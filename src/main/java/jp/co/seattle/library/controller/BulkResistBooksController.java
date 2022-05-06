package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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


/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkResistBooksController {
    final static Logger logger = LoggerFactory.getLogger(BulkResistBooksController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/bulkResistBooks", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulkResistBooks(Model model) {
        return "bulkResistBooks";
    }
   
    /**
     * 書籍情報を更新する
     * @param locale ロケール情報
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    
    @Transactional
    @RequestMapping(value = "/bulkResistBooks", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkResistBooks(Locale locale,@RequestParam("file") MultipartFile file, Model model) {
    	List<BookDetailsInfo> regist = new ArrayList<BookDetailsInfo>();
    	List<String> bulkError = new ArrayList<String>();
    	
    	// brにCSVファイルが入ってる状態
        try (BufferedReader br = new BufferedReader(
          //ファイルの内容を読み込み、変数に格納する
        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))){
          String line="";
          int count=0;
         
          // readLineメソッドで1行ずつ読み込んでnull値が出力されるまで読み込む。
          while ((line = br.readLine()) != null) {
        	 
            final String[] split = line.split(",", -1);
            count ++;
            
            // パラメータで受け取った書籍情報をDtoに格納する。
            BookDetailsInfo bookInfo = new BookDetailsInfo();
            bookInfo.setTitle(split[0]);
            bookInfo.setAuthor(split[1]);
            bookInfo.setPublisher(split[2]);
            bookInfo.setPublishDate(split[3]);
            bookInfo.setIsbn(split[4]);
            
            //validation check
            boolean hissuCheckNull = (split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty() || split[3].isEmpty());
            boolean PublishDateCheck = split[3].matches("^[0-9]{8}$");
            boolean ISBNCheck = split[4].matches("^[0-9]{10}||[0-9]{13}$");
            
            if (!ISBNCheck||!PublishDateCheck||hissuCheckNull) {
            //リストにエラーをためる
           bulkError.add(count + "行目でバリデーションエラーが起きました。");
                     }
           regist.add(bookInfo);
           
          }
          

          if(regist.size() == 0) {
        	  model.addAttribute("nofile", "ファイルの中身がありません。");
        	  return "bulkResistBooks";
          }
          
        } catch (IOException e) {  
        	throw new RuntimeException("ファイルが読み込めません。", e);
        }
        
        //エラーメッセージを表示した一括登録画面に戻す
        if(bulkError.size()>0) {
        model.addAttribute("bulkError", bulkError);
        return "bulkResistBooks";
        }
        //書籍情報を一冊ずつ新規登録するのを繰り返す
        for (BookDetailsInfo bookInfo : regist){ 
        	booksService.registBook(bookInfo); 
        	}
      
        //新規登録が完了し、ホーム画面に戻る
        model.addAttribute("bookList",booksService.getBookList());
        return "home";
       
        }
    }

