<template>
  <div class="app-container">
    <el-input
      placeholder="输入关键字查找"
      v-model="filterText"
      style="margin-bottom:30px;"
    />

    <el-tree
      ref="tree"
      :data="subjects"
      :props="defaultProps"
      :filter-node-method="filterNode"
      class="filter-tree"
      default-expand-all
    />
  </div>
</template>

<script>
import subjectApi from "@/api/edu/subject";

export default {
  data() {
    return {
      filterText: "", //过滤信息
      subjects: [],
      defaultProps: {
        children: "children",
        label: "title"
      }
    };
  },
  created() {
    this.listSubjects();
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    } 
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.title.toLowerCase().indexOf(value) !== -1;
    },
    //查找所有课程分类
    listSubjects() {
      subjectApi.getSubjectList().then(response => {
        console.log(response);
        this.subjects = response.data.subjects;
      });
    }
  }
};
</script>
