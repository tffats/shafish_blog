## 索引、文档和字段
### 索引

索引是Elasticsearch中的基本存储单元，它是一个逻辑命名空间，用于存储具有相似特征的数据，其是由名称或别名唯一标识的文档集合。

每个索引都有一个映射（mapping）或模式，用于说明如何对文档中的字段进行索引，映射定义了每个字段的数据类型、字段的索引方式以及存储方式。

### 文档

Es以JSON文档的形式序列化和存储数据。文档是一组字段，它们是包含数据的键值对。每个文档都有一个唯一的ID。

``` json
{
  "_index": "my-first-elasticsearch-index",
  "_id": "DyFpo5EBxE8fzbb95DOa",
  "_version": 1,
  "_seq_no": 0,
  "_primary_term": 1,
  "found": true,
  "_source": {
    "email": "john@smith.com",
    "first_name": "John",
    "last_name": "Smith",
    "info": {
      "bio": "Eco-warrior and defender of the weak",
      "age": 25,
      "interests": [
        "dolphins",
        "whales"
      ]
    },
    "join_date": "2024/05/01"
  }
}
```

### 字段

元数据是存储有关文档的信息的系统字段，其前缀是下划线。

- _index：存储文档的索引的名称
- _id：文档的ID。每个索引的ID必须唯一

## 搜索API

| 可用查询          | api point | 描述                                                                                     |
| ----------------- | --------- | ---------------------------------------------------------------------------------------- |
| Query DSL         | _search   | 主要查询语言。一种强大而灵活的JSON风格语言，支持复杂的查询                               |
| ES/QL  ES/ QL     | _query    | 8.11中引入，用于过滤、转换和分析数据的管道查询语言                                       |
| EQL               | _eql      | 事件查询语言,用于基于事件的时间序列数据的查询语言。数据必须包含@timestamp字段才能使用EQm |
| Elasticsearch SQL | _sql      | 原生的、类似SQL的实时查询                                                                |
| KQL               |           | 基于文本的查询语言，用于在您通过Kibana UI访问数据时过滤数据                              |

## 使用

### 创建索引

`PUT /books`

### 添加文档到索引

```
POST books/_doc
{
  "name": "Snow Crash",
  "author": "Neal Stephenson",
  "release_date": "1992-06-01",
  "page_count": 470
}
```
``` json
{
  "_index": "books", //文档被添加到的索引
  "_id": "CxUYcJUBH6x4E-MDS_Ph", //文档的唯一标识符
  "_version": 1, //文档的版本
  "result": "created", //索引操作的结果
  "_shards": { // 执行索引操作的分片数量以及成功执行的分片数量的信息
    "total": 2, // 索引的分片总数
    "successful": 2, //执行索引操作的分片数
    "failed": 0 //索引操作期间失败的分片数。0表示无故障
  },
  "_seq_no": 0, //分片上的每个索引操作保存一个单调递增的数字
  "_primary_term": 1 // 一个单调递增的数字，每次主分片被分配给不同的节点时递增
}
```

### 添加多个文档到索引

``` json
POST /_bulk
{ "index" : { "_index" : "books" } }
{"name": "Revelation Space", "author": "Alastair Reynolds", "release_date": "2000-03-15", "page_count": 585}
{ "index" : { "_index" : "books" } }
{"name": "1984", "author": "George Orwell", "release_date": "1985-06-01", "page_count": 328}
{ "index" : { "_index" : "books" } }
{"name": "Fahrenheit 451", "author": "Ray Bradbury", "release_date": "1953-10-15", "page_count": 227}
{ "index" : { "_index" : "books" } }
{"name": "Brave New World", "author": "Aldous Huxley", "release_date": "1932-06-01", "page_count": 268}
{ "index" : { "_index" : "books" } }
{"name": "The Handmaids Tale", "author": "Margaret Atwood", "release_date": "1985-06-01", "page_count": 311}
```

### 定义映射、数据类型

如果创建索引时没有指定映射，默认会自动为新字段创建映射

``` shell
GET /books/_mapping
```

- 创建显式映射
``` shell
PUT /my-explicit-mappings-books
{
  "mappings": {
    "dynamic": false,  
    "properties": {  
      "name": { "type": "text" },
      "author": { "type": "text" },
      "release_date": { "type": "date", "format": "yyyy-MM-dd" },
      "page_count": { "type": "integer" }
    }
  }
}
```
- dynamic：禁用索引的动态映射。包含映射中未定义的字段的文档将被拒绝
- properties：对象定义此索引中文档的字段及其数据类型

### 搜索

- 搜索所有文档：`GET books/_search`
- 匹配查询（在books索引中搜索name字段中包含brave的文档）：
``` json
GET books/_search
{
"query": {
    "match": {
    "name": "brave"
    }
}
}
```

- 删除索引：
```shell
DELETE /books
DELETE /my-explicit-mappings-books
```

### 更多示例

- 创建cooking_blog索引
```json
PUT /cooking_blog
```

- 创建映射
```json
PUT /cooking_blog/_mapping
{
  "properties": {
    "title": {
      "type": "text",
      "analyzer": "standard", 
      "fields": { 
        "keyword": {
          "type": "keyword",
          "ignore_above": 256 
        }
      }
    },
    "description": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword"
        }
      }
    },
    "author": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword"
        }
      }
    },
    "date": {
      "type": "date",
      "format": "yyyy-MM-dd"
    },
    "category": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword"
        }
      }
    },
    "tags": {
      "type": "text",
      "fields": {
        "keyword": {
          "type": "keyword"
        }
      }
    },
    "rating": {
      "type": "float"
    }
  }
}
```

- 添加数据
``` json
POST /cooking_blog/_bulk?refresh=wait_for
{"index":{"_id":"1"}}
{"title":"Perfect Pancakes: A Fluffy Breakfast Delight","description":"Learn the secrets to making the fluffiest pancakes, so amazing you won't believe your tastebuds. This recipe uses buttermilk and a special folding technique to create light, airy pancakes that are perfect for lazy Sunday mornings.","author":"Maria Rodriguez","date":"2023-05-01","category":"Breakfast","tags":["pancakes","breakfast","easy recipes"],"rating":4.8}
{"index":{"_id":"2"}}
{"title":"Spicy Thai Green Curry: A Vegetarian Adventure","description":"Dive into the flavors of Thailand with this vibrant green curry. Packed with vegetables and aromatic herbs, this dish is both healthy and satisfying. Don't worry about the heat - you can easily adjust the spice level to your liking.","author":"Liam Chen","date":"2023-05-05","category":"Main Course","tags":["thai","vegetarian","curry","spicy"],"rating":4.6}
{"index":{"_id":"3"}}
{"title":"Classic Beef Stroganoff: A Creamy Comfort Food","description":"Indulge in this rich and creamy beef stroganoff. Tender strips of beef in a savory mushroom sauce, served over a bed of egg noodles. It's the ultimate comfort food for chilly evenings.","author":"Emma Watson","date":"2023-05-10","category":"Main Course","tags":["beef","pasta","comfort food"],"rating":4.7}
{"index":{"_id":"4"}}
{"title":"Vegan Chocolate Avocado Mousse","description":"Discover the magic of avocado in this rich, vegan chocolate mousse. Creamy, indulgent, and secretly healthy, it's the perfect guilt-free dessert for chocolate lovers.","author":"Alex Green","date":"2023-05-15","category":"Dessert","tags":["vegan","chocolate","avocado","healthy dessert"],"rating":4.5}
{"index":{"_id":"5"}}
{"title":"Crispy Oven-Fried Chicken","description":"Get that perfect crunch without the deep fryer! This oven-fried chicken recipe delivers crispy, juicy results every time. A healthier take on the classic comfort food.","author":"Maria Rodriguez","date":"2023-05-20","category":"Main Course","tags":["chicken","oven-fried","healthy"],"rating":4.9}
```

- 匹配查询
``` json
// 匹配描述字段中包含“fluffy”或“pancakes”或两者都包含的文档
GET /cooking_blog/_search
{
  "query": {
    "match": {
      "description": {
        "query": "fluffy pancakes" 
      }
    }
  }
}
```
``` json
// 匹配描述中同时包含“fluffy”和“pancakes”的文档
GET /cooking_blog/_search
{
  "query": {
    "match": {
      "description": {
        "query": "fluffy pancakes", 
        "operator": "and"
      }
    }
  }
}
```
``` json
// 搜索标题字段以匹配3个术语中的至少2个：“fluffy”、“pancakes”或“breakfast”
GET /cooking_blog/_search
{
  "query": {
    "match": {
      "title": {
        "query": "fluffy pancakes breakfast",
        "minimum_should_match": 2
      }
    }
  }
}
```
``` json
// 在标题、描述和标签字段中搜索vegetarian curry，同时搜索多个字段
GET /cooking_blog/_search
{
  "query": {
    "multi_match": {
      "query": "vegetarian curry",
      "fields": ["title", "description", "tags"]
    }
  }
}
```
``` json
// 在标题、描述和标签字段中搜索vegetarian curry，设置匹配优先级。数字越大越优先
GET /cooking_blog/_search
{
  "query": {
    "multi_match": {
      "query": "vegetarian curry",
      "fields": ["title^3", "description^2", "tags"] 
    }
  }
}
```

- 过滤
```json
// 只返回“早餐”类别中的博客文章 category.keyword
GET /cooking_blog/_search
{
  "query": {
    "bool": {
      "filter": [
        { "term": { "category.keyword": "Breakfast" } }  
      ]
    }
  }
}
```
- 日期范围
```json
// 特定时间范围内发布的内容，大于或等于2023年5月1日，小于或等于2023年5月31日
GET /cooking_blog/_search
{
  "query": {
    "range": {
      "date": {
        "gte": "2023-05-01", 
        "lte": "2023-05-31" 
      }
    }
  }
}
```
- 完全匹配
``` json
// 在author.keyword字段中搜索作者“Maria Rodriguez”，term大小写敏感，避免在文本字段中使用
GET /cooking_blog/_search
{
  "query": {
    "term": {
      "author.keyword": "Maria Rodriguez" 
    }
  }
}
```
- 综合查询
```json
// 一定是素食食谱、标题或描述中应包含“咖喱”或“辣”、应该是主菜、一定不是甜点、必须有至少4.5的评级、应该更喜欢上个月出版的食谱
GET /cooking_blog/_search
{
  "query": {
    "bool": {
      "must": [
        { "term": { "tags": "vegetarian" } },
        {
          "range": {
            "rating": {
              "gte": 4.5
            }
          }
        }
      ],
      "should": [
        {
          "term": {
            "category": "Main Course"
          }
        },
        {
          "multi_match": {
            "query": "curry spicy",
            "fields": [
              "title^2",
              "description"
            ]
          }
        },
        {
          "range": {
            "date": {
              "gte": "now-1M/d"
            }
          }
        }
      ],
      "must_not": [ 
        {
          "term": {
            "category.keyword": "Dessert"
          }
        }
      ]
    }
  }
}
```

- 用avg聚合计算数据集中所有订单的平均订单值
```json
GET kibana_sample_data_ecommerce/_search
{
 "size": 0,  // 将size设置为0以避免在响应中返回匹配的文档，并仅返回聚合结果
 "aggs": {
   "avg_order_value": {  //一个有意义的名称，用于描述此指标所代表的内容
     "avg": {  //配置一个avg聚合，它计算一个简单的算术平均值
       "field": "taxful_total_price"
     }
   }
 }
}
```

- 使用stats聚合在一个请求中计算有关订单的多个统计信息
```json
GET kibana_sample_data_ecommerce/_search
{
 "size": 0,
 "aggs": {
   "order_stats": { 
     "stats": {  //返回count, min, max, avg, and sum
       "field": "taxful_total_price"
     }
   }
 }
}
```
- 使用terms聚合按类别对订单进行分组，以查看哪些产品类别最受欢迎
```json
GET kibana_sample_data_ecommerce/_search
{
 "size": 0,
 "aggs": {
   "sales_by_category": { 
     "terms": { 
       "field": "category.keyword", // 分组字段
       "size": 5, //输出5个
       "order": { "_count": "desc" } //订单数量排序（降序）
     }
   }
 }
}
```
- 按天对订单进行分组，以使用date_histogram聚合跟踪每日销售模式
```json
GET kibana_sample_data_ecommerce/_search
{
 "size": 0,
 "aggs": {
   "daily_orders": { 
     "date_histogram": {  //date_histogram聚合将文档分组到基于时间
       "field": "order_date",
       "calendar_interval": "day", 
       "format": "yyyy-MM-dd", 
       "min_doc_count": 0  //当min_doc_count为0时，返回无订单天数的存储桶，这对于连续时间序列可视化很有用。
     }
   }
 }
}
```