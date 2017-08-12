package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class ChapterController extends Controller
{
    public Result index()
    {
        return ok(views.html.chapters.render());
    }
}
