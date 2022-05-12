<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="teacherQuery.name" placeholder="讲师名" />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="teacherQuery.level"
          clearable
          placeholder="讲师头衔"
        >
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>
      <el-form-item label="添加时间">
        <el-date-picker
          v-model="teacherQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="teacherQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()"
        >查询</el-button
      >
      <el-button type="default" @click="clearTeacherQuery()">清空</el-button>
    </el-form>

    <!-- 表格 -->
    <el-table
      :data="teacherList"
      :row-class-name="tableRowClassName"
      fit
      highlight-current-row
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (pageCurrent - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="名称"
        width="90"
      />
      <el-table-column label="头衔" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.level === 1 ? "高级讲师" : "首席讲师" }}
        </template>
      </el-table-column>
      <el-table-column prop="intro" label="简介" />
      <el-table-column
        prop="gmtCreate"
        label="添加时间"
        width="200"
        align="center"
      />
      <el-table-column prop="sort" label="排序" width="60" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/teacher/edit/' + scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit"
              >修改</el-button
            >
          </router-link>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)"
            >删除</el-button
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
import teacher from "@/api/edu/teacher";

export default {
  //写代码
  data() {
    return {
      pageCurrent: 1, //当前页
      pageSize: 10, //每页条数
      //绑定后其实不需要在里面定义属性,会自己注入,这里为了便于理解
      teacherQuery: {
        name: "",
        level: "",
        gmtCreate: ""
      }, //查询条件
      total: 0, //总记录数
      teacherList: [] //返回的集合
    };
  },
  created() {
    this.getList();
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
      teacher
        .getTeacherList(Current, this.pageSize, this.teacherQuery)
        .then(response => {
          //   response.data.teacher
          //   console.log(response);
          this.teacherList = response.data.teachers;
          this.total = response.data.total;
          //   console.log(this.teacherList);
          //   console.log(this.total);
        })
        .catch(error => {
          console.log(error);
        });
    },
    clearTeacherQuery() {
      //清除表单查询
      this.teacherQuery = {};
      //重新加载
      this.getList();
    },
    // handleEdit(row) {
    //   console.log(row);
    // },
    //根据 Id 删除数据
    removeById(id) {
      this.$confirm("此操作将永久删除讲师记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          teacher
            .removeTeacherList(id)
            .then(response => {
              this.$message.success("删除成功!");
              this.getList();
            })
            .catch(error => {});
        })
        .catch(() => {});
    }
  }
};
</script>
