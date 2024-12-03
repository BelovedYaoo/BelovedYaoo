package top.belovedyaoo.agcore.toolkit;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ListString 类型处理器
 *
 * @author BelovedYaoo
 * @version 1.0
 */
public class ListStringTypeHandler implements TypeHandler<List<String>> {

    private static final String LEFT_FLAG = "[";
    private static final String RIGHT_FLAG = "]";

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return convertStringToList(rs.getString(columnName));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertStringToList(rs.getString(columnIndex));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertStringToList(cs.getString(columnIndex));
    }

    private static List<String> convertStringToList(String input) {
        // 首先检查输入是否为空
        if (input == null) {
            return new ArrayList<>();
        }
        // 检查输入是否不符合预期格式
        if (!input.startsWith(LEFT_FLAG) || !input.endsWith(RIGHT_FLAG)) {
            throw new IllegalArgumentException("输入格式不正确！");
        }

        // 去掉首尾的 ["] 和 \"]
        String trimmedInput = input.substring(1, input.length() - 1);

        // 使用逗号加空格作为分隔符来分割字符串
        String[] items = trimmedInput.split(", ");

        // 将数组转换为列表
        return new ArrayList<>(Arrays.asList(items));
    }

}