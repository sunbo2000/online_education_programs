<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="courseQuery.title" placeholder="课程名称" />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="courseQuery.teacherId"
          filterable
          placeholder="讲师名称"
        >
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>
      <!-- 分类查询 -->
      <el-form-item>
        <el-select
          v-model="courseQuery.subjectParentId"
          filterable
          placeholder="一级分类"
          @change="oneSubjectChange"
        >
          <el-option
            v-for="oneSubject in oneSubjectList"
            :key="oneSubject.id"
            :label="oneSubject.title"
            :value="oneSubject.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="courseQuery.subjectId"
          filterable
          placeholder="二级分类"
          @change="refresh"
        >
          <el-option
            v-for="twoSubject in twoSubjectList"
            :key="twoSubject.id"
            :label="twoSubject.title"
            :value="twoSubject.id"
          />
        </el-select>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="clearcourseQuery()">清空</el-button>
    </el-form>
    <!-- ************************************************************************* -->
    <!-- 表格 -->
    <el-table
      :data="courseList"
      :row-class-name="tableRowClassName"
      fit
      highlight-current-row
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (pageCurrent - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="课程名称" width="200" />
      <!-- <el-table-column
        prop="teacherId"
        label="讲师"
        width="100"
        align="center"
      />
      <el-table-column label="课程分类" align="center">
          <el-table-column
        prop="subjectParentId"
        label="一级分类"
        width="100"
        align="center"
      />
       <el-table-column
        prop="subjectId"
        label="二级分类"
        width="100"
        align="center"
      />
           </el-table-column> -->
      <el-table-column label="课程状态" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.status === "Normal" ? "已发布" : "未发布" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="lessonNum"
        label="课时数"
        sortable
        width="100"
        align="center"
      />
      <el-table-column
        prop="price"
        label="课程价格"
        sortable
        width="105"
        align="center"
      />
      <el-table-column
        prop="gmtCreate"
        label="添加时间"
        sortable
        width="200"
        align="center"
      />
      <el-table-column
        prop="viewCount"
        label="浏览次数"
        sortable
        width="105"
        align="center"
      />
      <el-table-column
        prop="buyCount"
        label="购买人数"
        sortable
        width="105"
        align="center"
      />
      <el-table-column label="操作" width="350" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"
              >修改信息</el-button
            >
          </router-link>
          <router-link :to="'/course/chapter/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"
              >修改大纲</el-button
            >
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)"
            >删除课程</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="pageCurrent"
      :page-size="pageSize"
      :total="total"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"
    />
  </div>
</template>

//给表格增加颜色
<style>
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
</style>

<script>
import course from "@/api/edu/course";
import subject from "@/api/edu/subject";

export default {
  //写代码
  data() {
    return {
      pageCurrent: 1, //当前页
      pageSize: 10, //每页条数
      //绑定后其实不需要在里面定义属性,会自己注入
      courseQuery: {}, //查询条件
      teacherList: [],
      oneSubjectList: [],
      twoSubjectList: [],
      total: 0, //总记录数
      courseList: [] //返回的集合
    };
  },
  created() {
    this.getList();
    this.getTeachers();
    this.getOneSubject();
  },
  methods: {
    //给表格增加颜色的方法
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 4 === 1) {
        return "warning-row";
      } else if (rowIndex % 4 === 3) {
        return "success-row";
      }
      return "";
    },
    //Current 表示当前查询的页码值,它的值是自动封装识别的,em 甚至可以不要 data 里的 pageCurrent
    getList(Current = 1) {
      this.pageCurrent = Current;
      course
        .getcourseList(Current, this.pageSize, this.courseQuery)
        .then(response => {
          //   response.data.teacher
          //   console.log(response);
          this.courseList = response.data.courses;
          this.total = response.data.total;
          //   console.log(this.courseList);
          //   console.log(this.total);
        })
        .catch(error => {
          console.log(error);
        });
    },
    //获取所有讲师信息
    getTeachers() {
      course.getAllTeachers().then(response => {
        this.teacherList = response.data.teachers;
      });
    },
    //获取一级分类
    getOneSubject() {
      subject.getSubjectList().then(response => {
        this.oneSubjectList = response.data.subjects;
      });
    },
    //一级分类改变//value 为v-model绑定值
    oneSubjectChange(value) {
      this.courseQuery.subjectId = "";
      for (var i = 0; i < this.oneSubjectList.length; i++) {
        if (this.oneSubjectList[i].id == value) {
          this.twoSubjectList = this.oneSubjectList[i].children;
        }
      }
    },
    //强制刷新二级分类选择器,解决有数据但选不了的尴尬局面
    refresh() {
      this.$forceUpdate(); //强制更新
    },
    clearcourseQuery() {
      //清除表单查询
      this.courseQuery = {};
      //重新加载
      this.getList();
    },
    // handleEdit(row) {
    //   console.log(row);
    // },
    //根据 Id 删除数据
    removeById(id) {
      this.$confirm("此操作将永久删除课程信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          course.deleteById(id).then(response => {
            this.$message.success("删除课程信息成功");
            this.getList();
          });
        })
        .catch(() => {});
    }
  }
};
</script>
