package ra.run;
import ra.entity.Book;
import ra.entity.Category;
import ra.file.FileManager;

import java.util.List;
import java.util.stream.Collectors;
import static ra.run.Library.bookList;
import static ra.run.Library.categoryList;
import static ra.entity.Category.scanner;
public class BookMenu {
    public static void bookMenu() {
        do {
            System.out.println("===== QUẢN LÝ SÁCH =====");
            System.out.println("1. Thêm mới sách");
            System.out.println("2. Cập nhật thông tin sách");
            System.out.println("3. Xóa sách");
            System.out.println("4. Tìm kiếm sách ");
            System.out.println("5. Hiển thị danh sách sách theo nhóm thể loại");
            System.out.println("6. Quay lại");
            System.out.println("==========================");
            System.out.print("Chọn : \t");
            try {
                int choiceBook = Integer.parseInt(scanner.nextLine());
                System.out.println();
                switch (choiceBook){
                    case 1:
                        saveBook();
                        break;
                    case 2:
                        updateBook();
                        break;
                    case 3:
                        deleteBook();
                        break;
                    case 4:
                        searchBook();
                        break;
                    case 5:
                        displayBookByCatalogId();
                        break;
                    case 6:
                        FileManager.writeDataToBookFile(bookList);
                        return;
                    default:
                        System.err.println("vui lòng nhập lại từ 1 đến 6!");
                }
            } catch (NumberFormatException ex1){
                System.err.println("Lỗi đinh dạng chữ. Vui lòng nhập lại!");
            }
        } while(true);
    }
    // thêm mới sách
    public static void saveBook(){
        if (categoryList.isEmpty()){
            System.err.println("Không có thể loại nào để thêm mới sách. Vui lòng thêm thể loại.");
            return;
        }
        System.out.println("✰✰✰✰✰✰✰✰✰✰Danh sách thể loại✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰✰ ");
        for (Category category : categoryList) {
            System.out.println("\tMã thể loại :" + category.getId() + "\t-\tTên thể loại :" + category.getName());
        }
        System.out.println("Nhập số lượng sách cần thêm : ");
        do {
            try {
                int number = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < number; i++) {
                    System.out.println("Nhập vào mã thể loại cho sách thứ " + (i+1) +" :");
                    int categoryId = Integer.parseInt(scanner.nextLine());
                    Category selectedCategory = categoryList.stream().filter(category ->
                            category.getId() == categoryId).findFirst().orElse(null);
                    if (selectedCategory == null){
                        System.err.println("Không tìm thấy thể loại bằng mã thể loại đã nhập. Không thêm sách.");
                        continue;
                    }
                    Book newBook = new Book();
                    newBook.input();
                    bookList.add(newBook);
                    System.out.println("Đã thêm mới sách thứ " +(i+1) +" thành công ✔︎");
                    System.out.println("❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆❆");
                }
                break;
            } catch (NumberFormatException e){
                System.err.println("Số lượng sách phải là số. Vui lòng nhập lại.");
            } catch (Exception exception){
                System.err.println("Xảy ra lỗi. Vui lòng liên hệ tới hệ thống.");
            }
        } while (true);

    }
    // cập nhật thông tin sách
    public static void updateBook(){
        if (bookList.isEmpty()){
            System.err.println("Không có sách nào để cập nhật.");
            return;
        }
        System.out.println("Nhập vào mã sách cần cập nhật :");
        String updateBookId = scanner.nextLine();
        int index = -1;
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getId().equals(updateBookId)){
                index = i;
                break;
            }
        }
        if (index != -1){
            // cập nhật tiếp thông tin sách
            bookList.get(index).setTitle(Book.validateBookTitle());
            bookList.get(index).setAuthor(Book.validateBookAuthor());
            bookList.get(index).setPublisher(Book.validateBookPusblisher());
            bookList.get(index).setYear(Book.validateBookYear());
            bookList.get(index).setDescription(Book.validateBookDescription());
        } else {
            System.err.println("Không tồn tại mã sinh viên " + updateBookId +".");
        }
    }
    // xoá sách
    public static void deleteBook() {
        if (bookList.isEmpty()) {
            System.err.println("không có sách nào để xoá.");
            return;
        }
        System.out.println("Nhập mã sách cần xoá :");
        String deleteBookID = scanner.nextLine();
        Book foundBook = bookList.stream().filter(book -> book.getId().equals(deleteBookID)).findFirst().orElse(null);
        if (foundBook == null) {
            System.err.println("Không tìm thấy sách đã nhập.");
        } else {
            bookList.remove(foundBook);
            System.out.println("Xoá sách thành công ✔︎");
        }
    }
    // tìm kiếm sách
    public static void searchBook(){
        System.out.println("Nhập vào từ khoá cần tìm :");
        String keyword = scanner.nextLine().toLowerCase();
        List<Book> searchResult = bookList.stream().filter(book ->
                book.getId().toLowerCase().contains(keyword) ||
                book.getTitle().toLowerCase().contains(keyword) ||
                book.getAuthor().toLowerCase().contains(keyword) ||
                book.getDescription().toLowerCase().contains(keyword)).collect(Collectors.toList());
        if (searchResult.isEmpty()){
            System.err.println("Không tìm thấy sách phù hợp với từ khoá cần tìm.");
        } else{
            System.out.println("Các sách phù hợp với từ khoá cần tìm là :");
            for (Book book: searchResult) {
                book.output();
            }
        }
    }
    // hiển thị danh sách sách theo nhóm thể loại
    public static void displayBookByCatalogId(){
        // kiểm tra xem list sách vs thể loại có tòn rại hay không
        if (categoryList.isEmpty() || bookList.isEmpty()){
            System.err.println("Không có thể loại hoặc sách để hiển thị.");
            return;
        }
        System.out.println("Danh sách sách theo nhóm thể loại ");
        for (Category cate: categoryList) {
            System.out.println("Thể loại :" + cate.getName());
            List<Book> bookInCategory = bookList.stream().filter(book -> book.getCatalogId() == cate.getId()).collect(Collectors.toList());
            for (Book book : bookInCategory) {
                book.output();
            }
            System.out.println();
        }
    }
}
