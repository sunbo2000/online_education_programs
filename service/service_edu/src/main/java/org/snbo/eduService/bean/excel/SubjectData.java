package org.snbo.eduService.bean.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author sunbo
 * @create 2022-03-23-21:43
 */

@Data
public class SubjectData {
    //excel 表格的第一行是作为表头的,即下面的一级分类和二级分类
    //从第二行开始读取数据
    @ExcelProperty(value = "一级分类", index = 0)
    private String oneSubjectName;
    @ExcelProperty(value = "二级分类", index = 1)
    private String twoSubjectName;

}
