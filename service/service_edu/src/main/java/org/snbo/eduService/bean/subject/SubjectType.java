package org.snbo.eduService.bean.subject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 前端需要特定格式数据返回
 * @author sunbo
 * @create 2022-03-25-22:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectType {
    private String id;
    private String title;
    List<SubjectType> children;
}
