package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {
    // Cria os filtros da pesquisa em paginação, por course level, course status e name, Sendo Equal para exatamente igual e Like para aproximado
    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @And(@Spec(path = "title", spec = Like.class))
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @And(@Spec(path = "title", spec = Like.class))
    public interface LessonSpec extends Specification<LessonModel> {}

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, cb) -> {
            // Para não ter resultados duplicados
            query.distinct(true);
            // Entidade A
            Root<ModuleModel> module = root;
            // Entidade B
            Root<CourseModel> course = query.from(CourseModel.class);
            // Extrai a coleção da Entidade A na Entidade B | Extraindo todos os modulos de um determinado curso
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            // Constroi a Query
            return cb
                    // Filtra da Entidade B todos os cursos com o id courseId
                    .and(cb.equal(course.get("courseId"), courseId),
                            // Subselect, faz verificação pra saber quais os modulos estao dentro dessa coleção que esta dentro deste curso.
                            cb.isMember(module, coursesModules));
        };
    }
}
