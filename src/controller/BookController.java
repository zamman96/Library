package controller;

import print.Print;
import service.BookService;

public class BookController extends Print {
	BookService bookService = BookService.getInstance();
}
