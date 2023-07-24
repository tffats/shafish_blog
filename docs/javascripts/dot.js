doT.templateSettings = {
    evaluate:    /\{\{([\s\S]+?)\}\}/g,
    interpolate: /\{\{=([\s\S]+?)\}\}/g,
    encode:      /\{\{!([\s\S]+?)\}\}/g,
    use:         /\{\{#([\s\S]+?)\}\}/g,
    define:      /\{\{##\s*([\w\.$]+)\s*(\:|=)([\s\S]+?)#\}\}/g,
    conditional: /\{\{\?(\?)?\s*([\s\S]*?)\s*\}\}/g,
    iterate:     /\{\{~\s*(?:\}\}|([\s\S]+?)\s*\:\s*([\w$]+)\s*(?:\:\s*([\w$]+))?\s*\}\})/g,
    varname: 'it',
    strip: true,
    append: true,
    selfcontained: false
  };

// 默认模板定义
var defaultTempl = "<div><a>name:{{= it.name}}</a ><p>age:{{= it.age}}</p ><p>hello:{{= it.sayHello() }}</p >{{~ it.arr:item}}<p>INSERT INTO aria2_file(REVISION,CREATED_BY,CREATED_TIME,UPDATED_BY,UPDATED_TIME,NAME) VALUES('{{=item.version}}','{{=item.create}}','{{=item.createTime}}','{{=item.update}}','{{=item.updateTime}}','{{=item.name}}');</p>{{~}}</div>"

// 模板数据定义
var defaultTemplData = {
    name:'stringParams1',
    stringParams1:'stringParams1_value',
    age: 21,
    stringParams2:1,
    arr:[
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish4', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish5', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish6', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish7', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish8', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish9', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish10', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish11', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish12', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish13', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish14', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish15', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish16', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish17', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish18', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish19', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish20', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish21', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish22', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish23', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish24', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish25', text:'val1值'},
    {version:0, create:'shafish', createTime:'2023-06-10 15:41:57', update:'shafish', updateTime:'2023-06-10 15:45:57', name:'shafish26', text:'val1值'}
    ],
    sayHello:function () {
        return this[this.name]
    }
}

var testdd = document.getElementById("fisha_dot_jsondata").value;

// dot替换
function refreshDot() {
    var self_templ = document.getElementById("fisha_dot_templ").value.replace(/(\r|\n|\r)/gm, "").trim();
    var self_data = document.getElementById("fisha_dot_jsondata").value.replace(/(\r|\n|\r)/gm, "").trim();

    var templ = (self_templ !== "") ? self_templ : defaultTempl;

    var data = (self_data !== "") ? JSON.parse(self_data) : defaultTemplData;
    data.sayHello = function () {
        return this.name
    }

    data.camel = function(str, capitalizeFirstLetter) {
        var words = str.split('_');
        var camelCaseStr = words[0];
        
        for (var i = 1; i < words.length; i++) {
          var capitalizedWord = words[i].charAt(0).toUpperCase() + words[i].slice(1);
          camelCaseStr += capitalizedWord;
        }
        
        if (capitalizeFirstLetter) {
          camelCaseStr = camelCaseStr.charAt(0).toUpperCase() + camelCaseStr.slice(1);
        }
        
        return camelCaseStr;
    }

    data.convertToUnderscore = function(str, uppercase) {
        var underscoreStr = "";
        
        for (var i = 0; i < str.length; i++) {
          if (str.charAt(i) === str.charAt(i).toUpperCase() && i > 0) {
            underscoreStr += "_" + str.charAt(i).toLowerCase();
          } else {
            underscoreStr += str.charAt(i);
          }
        }
        
        if (uppercase) {
          underscoreStr = underscoreStr.toUpperCase();
        }
        
        return underscoreStr;
    }

    data.convertToUpperCase = function(str) {
        return str.toUpperCase();
    }

    data.convertToLowerCase = function(str) {
        return str.toLowerCase();
    }

    var fishaDot = doT.template(templ);
    var fishaDotData = fishaDot(data);

    document.getElementById("fisha_dot_view").innerHTML = fishaDotData;
} 

refreshDot()