package net.wanho.service_search.service;

import net.wanho.common.util.StringUtils;
import net.wanho.common.vo.response.PageInfo;
import net.wano.po.course.CoursePubDocument;
import net.wano.po.search.CourseSearchParam;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsCourseService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    //课程搜索
    public PageInfo<CoursePubDocument> list(int page, int size, CourseSearchParam courseSearchParam) {
        //校验
        if(StringUtils.isNull(courseSearchParam)){
            courseSearchParam = new CourseSearchParam();
        }

        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }

        //分页高亮搜索
        String preTags = "<font class='eslight'>";
        String postTags = "</font>";

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withPageable(PageRequest.of(page - 1, size))
                .withHighlightFields(new HighlightBuilder.Field("name").preTags(preTags).postTags(postTags));
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            nativeSearchQueryBuilder.withQuery(multiMatchQueryBuilder);
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            //根据一级分类
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            //根据二级分类
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            //根据难度等级
            nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("grade", courseSearchParam.getGrade()));
        }
        //创建搜索对象
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        //创建要返回的页面对象
        PageInfo<CoursePubDocument> pubPageInfo = new PageInfo<>();

        //es搜索
        AggregatedPage<CoursePubDocument> pageInfo = elasticsearchTemplate.queryForPage(searchQuery, CoursePubDocument.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                List<CoursePubDocument> coursePubs = new ArrayList<>();
                //获取响应结果
                SearchHits searchHits = response.getHits();
                pubPageInfo.setTotal(searchHits.getTotalHits());
                if (searchHits != null) {
                    //获取高亮中所有的内容
                    SearchHit[] hits = searchHits.getHits();
                    if (hits.length > 0) {
                        for (SearchHit hit : searchHits) {
                            CoursePubDocument coursePub = new CoursePubDocument();
                            //源文档
                            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                            //取出id
                            coursePub.setId((String) sourceAsMap.get("id"));

                            //获取高亮字段name
                            HighlightField highlightField1 = hit.getHighlightFields().get("name");

                            if(highlightField1 != null) {
                                //获取第一个字段的值并封装给实体类
                                String hight_value1 = highlightField1.getFragments()[0].toString();
                                coursePub.setName(hight_value1);
                            }else {
                                //获取原始的值
                                String value = (String) hit.getSourceAsMap().get("name");
                                coursePub.setName(value);
                            }
                            //图片
                            coursePub.setPic((String) sourceAsMap.get("pic"));
                            //价格
                            coursePub.setPrice((Double) sourceAsMap.get("price"));
                            //旧价格
                            coursePub.setPrice_old((Double) sourceAsMap.get("price_old"));

                            //将coursePub对象放入list
                            coursePubs.add(coursePub);
                        }
                    }
                }


                return new AggregatedPageImpl(coursePubs);
            }
        });

        pubPageInfo.setList(pageInfo.getContent());
        return pubPageInfo;

    }
}
