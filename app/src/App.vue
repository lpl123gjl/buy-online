<template>
  <div>
    <router-view></router-view>
  </div>
</template>



<style>
body,div,ul,li,h1,h2,h3,h4,h5,h6,p,ol,span,a,img,form{
    margin: 0;
    padding: 0;
}
html,body{
  height: 100%;
  background: linear-gradient(35deg, #f1ffff 0%, #fdf8ff 100%);
    font-family: Arial,'Microsoft Yahei';
}
ul{
    list-style: none;
}
a{
    text-decoration: none;
    color: inherit;
    -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
    -moz-user-focus: none;
}
a, a:visited, a:link, a:hover{
	color:inherit;
}


 
</style>