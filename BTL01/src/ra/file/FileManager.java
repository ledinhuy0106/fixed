package ra.file;

import ra.entity.Book;
import ra.entity.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final String CATEGORY_FILE = "categories.txt";
    public static final String BOOK_FILE = "books.txt";
    public static <T> void saveDataToFile(String fileName, List<T> dataList){
        File file = new File(fileName);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(dataList);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static <T> List<T> loadDataFromFile(String fileName) {
        File file = new File(fileName);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        List<T> dataList = new ArrayList<>(); // Khởi tạo danh sách trống nếu tệp không tồn tại hoặc rỗng
        try {
            if (file.exists() && file.length() > 0) { // Kiểm tra xem tệp có tồn tại và không rỗng không
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                dataList = (List<T>) ois.readObject();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dataList;
    }
    public static void writeDataToCategoryFile(List<Category> categoryList){
        saveDataToFile(CATEGORY_FILE,categoryList);
    }

    public static void writeDataToBookFile(List<Book> bookList){
        saveDataToFile(BOOK_FILE,bookList);
    }

}
