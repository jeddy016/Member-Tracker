package controllers;

import models.Chapter;
import models.MemberDetail;
import play.Logger;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchController extends Controller
{
    private final JPAApi jpaApi;
    private final FormFactory formFactory;

    @Inject
    public SearchController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional
    public Result applyFilters()
    {
        return ok();
    }

    @Transactional
    public Result search()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();

        String query;

        String applyFilters = form.get("apply-filters");
        String searchInput = form.get("input");

        if(applyFilters != null)
        {
            String chapterFilter = form.get("chapter-filter");
            String volunteerFilter = form.get("vol-filter");
            String dateFilter = form.get("date-filter");

            Map<String, String> filters = new HashMap<>();

            filters.put("chapter", chapterFilter);
            filters.put("volunteer", volunteerFilter);
            filters.put("date", dateFilter);

            query = filteredSearch(filters);
        }
        else
        {
            query = "SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE first_name LIKE :searchInput OR last_name LIKE :searchInput OR co.name LIKE :searchInput OR jt.name LIKE :searchInput ORDER BY last_name";
        }

        @SuppressWarnings("unchecked")
        List<MemberDetail> members = jpaApi.em().createNativeQuery(query, MemberDetail.class).setParameter("searchInput", "%" + searchInput + "%").getResultList();

        return ok(views.html.index.render(members, chapters));
    }

    @Transactional
    private String filteredSearch(Map<String, String> filters)
    {
        StringBuilder query = new StringBuilder("SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE (first_name LIKE :searchInput OR last_name LIKE :searchInput OR co.name LIKE :searchInput OR jt.name LIKE :searchInput)");

        for(Map.Entry<String, String> filter : filters.entrySet())
        {
            String filterType = filter.getKey();
            String input = filter.getValue();

            if(filterType.equals("chapter"))
            {
                if(!input.equals("N/A"))
                {
                    query.append(" AND ch.name LIKE \"").append(input).append("\" ");
                }
            }

            if(filterType.equals("volunteer"))
            {
                if(!input.equals("-1"))
                {
                    query.append(" AND m.volunteer = ").append(input);
                }
            }

            if(filterType.equals("date"))
            {
                if(!input.equals("-1"))
                {
                    if(input.equals("0"))
                    {
                        query.append(" AND TIMESTAMPDIFF(YEAR, m.date_joined, CURDATE()) <= 1 ");
                    }
                    if(input.equals("1"))
                    {
                        query.append(" AND (TIMESTAMPDIFF(YEAR, m.date_joined, CURDATE()) > 1) AND (TIMESTAMPDIFF(YEAR, date_joined, CURDATE()) < 3) ");
                    }
                    if(input.equals("2"))
                    {
                        query.append(" AND TIMESTAMPDIFF(YEAR, m.date_joined, CURDATE()) >= 3 ");
                    }
                }
            }
        }

        query.append(" ORDER BY last_name");

        return query.toString();
    }
}
