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

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

@Controller //APIの入り口
public class ReturnController {

	final static Logger logger = LoggerFactory.getLogger(ReturnController.class);
	
	 @Autowired
	    private RentService rentService;
	 @Autowired
	    private BooksService booksService;
	 
	 /**
	  * 
	  * @param locale
	  * @param bookId
	  * @param model
	  * @return details
	  */
	    @Transactional
	    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	    public String returnBook(Locale locale,
	            @RequestParam("bookId") int bookId,
	            Model model) {
	      logger.info("Welcome returnBook.java! The client locale is {}.", locale);
	      
	      int before = rentService.countId();
	      
	   // 書籍を返却する
	         rentService.returnBook(bookId);
	        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
	      
	        int after = rentService.countId();
	        
	      if (before == after) {
		      model.addAttribute("returnError","貸し出しされていません。");
		    	
				return "details";
		    }
	      
		return "details";
}
}
