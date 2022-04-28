package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

@Controller //APIの入り口
public class BulkResistBooksController {
    final static Logger logger = LoggerFactory.getLogger(BulkResistBooksController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/bulkResistBooks", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    //RequestParamでname属性を取得
    public String bulkResistBooks(Model model, Locale locale, @RequestParam ("bookId") int bookId) {
    	model.addAttribute("bookInfo",booksService.getBookInfo(bookId));
        return "BulkResistBooks";
    }

    /**
     * 書籍情報を登録する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/updateId", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateId(Locale locale,
    		@RequestParam("bookId") int bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("ISBN") String isbn,
            @RequestParam("explanation") String explanation,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setExplanation(explanation);
        
        
        boolean hissuCheckNull = (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty());
        boolean PublishDateCheck = publishDate.matches("^[0-9]{8}$");
        boolean ISBNCheck = isbn.matches("^[0-9]{10}||[0-9]{13}$");
        

        if (hissuCheckNull) {
        	
        	model.addAttribute("hissuError","必須項目を入力してください。");
        	
        }
        
        if (!PublishDateCheck) {
        	
        	model.addAttribute("dateError","出版日は、半角数字、YYYYMMDD形式で入力してください。");
        	
       } 
        
        if (!ISBNCheck) {
        	
        	model.addAttribute("ISBNError","ISBNは半角数字で、10桁か13桁で入力してください。");
        	
        }
        
        if (!ISBNCheck||!PublishDateCheck||hissuCheckNull) {
        	model.addAttribute("bookInfo", bookInfo);
        	return "bulkResistBooks";
        
        }
   
      
    }