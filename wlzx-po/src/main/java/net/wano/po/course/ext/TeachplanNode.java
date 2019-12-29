package net.wano.po.course.ext;


import net.wano.po.course.Teachplan;
import lombok.Data;
import lombok.ToString;


import java.util.List;


@Data
@ToString
public class TeachplanNode extends Teachplan {
    List<TeachplanNode> children;
}
