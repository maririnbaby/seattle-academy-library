package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

//import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HistoryController {
    static Logger logger = LoggerFactory.getLogger(HistoryController.class);

   // @Autowired
   // private BooksService booksService;
    @RequestMapping(value = "/historyBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String historyBook(Model model) {
        return "history";
    }
    @Transactional
    @RequestMapping(value = "/historyBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String historyBook(Locale locale,@RequestParam("file") MultipartFile file, Model model) {
        return "history";
    }
}