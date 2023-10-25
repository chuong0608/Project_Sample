import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVToSQLConverter {
    public static void main(String[] args) {
        String csvFile = "D:\\input.csv"; // Tên tệp CSV
        String sqlFile = "D:\\output.sql"; // Tên tệp .sql đầu ra

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             FileWriter writer = new FileWriter(sqlFile)) {

            String tableName = "your_table_name"; // Thay đổi thành tên bảng cơ sở dữ liệu của bạn
            String line;

            // Đọc dòng đầu tiên và tách các tên trường
            if ((line = reader.readLine()) != null) {
                String[] columns = parseCSVLine(line);

                while ((line = reader.readLine()) != null) {
                    String[] values = parseCSVLine(line);

                    // Xây dựng câu lệnh SQL INSERT
                    String insertSQL = buildInsertStatement(tableName, columns, values);

                    // Ghi câu lệnh SQL vào tệp .sql
                    writer.write(insertSQL);
                    writer.write("\n");
                }

                System.out.println("Chuyển đổi CSV thành SQL hoàn tất.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] parseCSVLine(String line) {
        String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        for (int i = 0; i < values.length; i++) {
            // Loại bỏ dấu nháy từ giá trị trong trường hợp có dấu nháy
            values[i] = values[i].replaceAll("\"", "");
        }
        return values;
    }

    private static String buildInsertStatement(String tableName, String[] columns, String[] values) {
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        for (String column : columns) {
            sql.append(column).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length()); // Loại bỏ dấu phẩy cuối cùng
        sql.append(") VALUES (");

        for (String value : values) {
            sql.append("'").append(value).append("', "); // Đảm bảo mỗi giá trị trong dấu nháy đơn
        }
        sql.delete(sql.length() - 2, sql.length()); // Loại bỏ dấu phẩy cuối cùng
        sql.append(");");

        return sql.toString();
    }
}
