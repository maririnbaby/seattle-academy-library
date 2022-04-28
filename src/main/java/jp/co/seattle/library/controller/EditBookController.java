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
import jp.co.seattle.library.service.ThumbnailService;

@Controller //APIの入り口
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

    @Autowired
    private BooksService booksService;
    

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/editBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    //RequestParamでname属性を取得
    public String editBook(Model model, Locale locale, @RequestParam ("bookId") int bookId) {
    	model.addAttribute("bookInfo",booksService.getBookInfo(bookId));
        return "editBook";
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

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }

        
        
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
        	return "editBook";
        
        }
   
        // 書籍情報を編集する
         booksService.editBook(bookInfo);
     	  
        // TODO 登録した書籍の詳細情報を表示するように実装
     	   int editId = bookInfo.getBookId();
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(editId));
        //  詳細画面に遷移する
        
        return "details";
        
    }
    
    }
