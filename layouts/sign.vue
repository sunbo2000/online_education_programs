<template>
  <div class="sign">
    <!--标题-->
    <div>
      <el-breadcrumb
        separator="/"
        style="position: absolute; left:275px; top: 53px"
      >
        <el-breadcrumb-item
          ><a style="cursor: pointer; font-size:20px" href="/">返回首页</a></el-breadcrumb-item
        >
      </el-breadcrumb>
      <img
        style="width: 250px; height: auto; position: absolute; left:15px; top:20px"
        src="~/assets/img/frontlogo.png"
        alt="logo"
      />
    </div>

    <!--表单-->

    <nuxt />
  </div>
</template>
