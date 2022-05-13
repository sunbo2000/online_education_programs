<template>
  <div class="app-container">
    <!-- ************************************************************************* -->
    <el-button type="primary" icon="el-icon-upload2" @click="openSaveInfo"
    >新增
    </el-button
    >
    <!-- ************************************************************************** -->
    <!-- 章节嵌套表单 -->
    <el-dialog title="添加轮播图" width="40%" :visible.sync="dialogFormVisible">
      <el-form :model="banner" ref="banner" :rules="rules">
        <el-form-item
          label="封面名称"
          :label-width="formLabelWidth"
          prop="title"
        >
          <el-input v-model="banner.title" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item
          label="封面"
          :label-width="formLabelWidth"
          prop="imageUrl"
        >
          <el-upload
            class="avatar-uploader"
            :action="BASE_API + '/eduOss/fileOss'"
            :show-file-list="false"
            accept="image/jpeg,image/jpg,image/png"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img :src="banner.imageUrl" style="width: 300px; height: 168px"/>
          </el-upload>
        </el-form-item>

        <el-form-item
          label="链接地址"
          :label-width="formLabelWidth"
          prop="linkUrl"
        >
          <el-input v-model="banner.linkUrl" auto-complete="off"></el-input>
        </el-form-item>

        <el-form-item label="封面排序" :label-width="formLabelWidth">
          <el-input-number
            v-model="banner.sort"
            :min="0"
            controls-position="center"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('banner')"
        >确 定
        </el-button
        >
      </div>
    </el-dialog>
    <!-- 表格 -->
    <el-table
      :data="bannerList"
      :row-class-name="tableRowClassName"
      fit
      highlight-current-row
    >
      <el-table-column label="序号" width="80" align="center">
        <template slot-scope="scope">
          {{ (pageCurrent - 1) * pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="title" label="封面标题" width="200"/>

      <el-table-column label="封面图片" width="310" align="center">
        <template slot-scope="scope">
          <img
            style="width: 250px; height: 140px; border: none"
            :src="scope.row.imageUrl"
          />
        </template>
      </el-table-column>

      <el-table-column
        prop="linkUrl"
        label="链接地址"
        width="280"
        align="center"
      />
      <el-table-column
        prop="sort"
        label="排序"
        sortable
        width="100"
        align="center"
      />
      <el-table-column
        prop="gmtCreate"
        label="添加时间"
        sortable
        width="200"
        align="center"
      />
      <el-table-column label="操作" width="240" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-edit"
            @click="openUpdateInfo(scope.row.id)"
          >修改
          </el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)"
          >删除
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="pageCurrent"
      :page-size="pageSize"
      :total="total"
      style="padding: 30px 0; text-align: center"
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
import banner from '@/api/edu/banner'

export default {
  //写代码
  data() {
    return {
      BASE_API: process.env.BASE_API,
      dialogFormVisible: false, //表单显示
      formLabelWidth: '120px',
      pageCurrent: 1, //当前页
      pageSize: 4, //每页条数
      //绑定后其实不需要在里面定义属性,会自己注入
      total: 0, //总记录数
      bannerList: [], //返回的集合
      banner: {},
      rules: {
        title: [
          { required: true, message: '请输入轮播图名称', tigger: 'blur' }
        ],
        linkUrl: [
          { required: true, message: '请输入链接地址', tigger: 'blur' }
        ],
        imageUrl: [
          { required: true, message: '请输入链接地址', tigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    //给表格增加颜色的方法
    tableRowClassName({ row, rowIndex }) {
      if (rowIndex % 4 === 1) {
        return 'warning-row'
      } else if (rowIndex % 4 === 3) {
        return 'success-row'
      }
      return ''
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //   //进行添加或修改操作
          this.saveOrUpdate()
          //   console.log("success");
        } else {
          console.log('表单内容输入有误,检查下吧')
          return false
        }
      })
    },
    saveOrUpdate() {
      if (!this.banner.id) {
        this.saveInfo()
      } else {
        this.updateInfo()
      }
    },
    updateInfo() {
      banner.updateBanner(this.banner).then((response) => {
        this.$message.success('修改轮播图成功')
        this.dialogFormVisible = false
        this.getList()
      })
    },
    saveInfo() {
      banner.saveBannerInfo(this.banner).then((response) => {
        this.$message.success('添加轮播图成功')
        this.dialogFormVisible = false
        this.getList()
      })
    },
    //弹出修改框
    openUpdateInfo(id) {
      banner.getBannerById(id).then((response) => {
        this.banner = response.data.banner
      })
      this.dialogFormVisible = true
    },
    //上传封面之前调用
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg'
      const isJPG1 = file.type === 'image/jpg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 5
      if (!isJPG && !isJPG1 && !isPNG) {
        this.$message.error('上传封面只能是 JPG/PNG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传封面大小不能超过 5MB!')
      }
      return (isJPG || isJPG1 || isPNG) && isLt2M
    },
    //上传封面成功回调方法
    handleAvatarSuccess(res, file) {
      this.banner.imageUrl = res.data.url
    },
    //弹出新增表单
    openSaveInfo() {
      this.banner = {
        imageUrl:
          'https://edu-mogu.oss-cn-chengdu.aliyuncs.com/%E4%B8%8A%E4%BC%A0.png',
        sort: 0
      }
      this.dialogFormVisible = true
    },
    //添加banner数据
    saveBannerInfo() {
      banner.saveBannerInfo(banner).then((response) => {
        this.$message.success('添加轮播图成功')
        this.getList()
      })
    },
    //Current 表示当前查询的页码值,它的值是自动封装识别的,em 甚至可以不要 data 里的 pageCurrent
    getList(Current = 1) {
      this.pageCurrent = Current
      banner
        .getPageBannerInfo(Current, this.pageSize)
        .then((response) => {
          this.bannerList = response.data.banners
          this.total = response.data.total
        })
        .catch((error) => {
          console.log(error)
        })
    },
    //根据 Id 删除数据
    removeById(id) {
      this.$confirm('此操作将永久删除轮播图, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          banner.deleteBannerInfo(id).then((response) => {
            this.$message.success('删除轮播图成功')
            this.getList()
          })
        })
        .catch(() => {
        })
    }
  }
}
</script>
