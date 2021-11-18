# CRUD

範例情境:

1. 建立使用者
2. 修改使用者
3. 修改使用者密碼, 檢核規則:
   1. 須至少5碼
   2. 至少包含一個數字
   3. 至少包含一個大寫及一個小寫英文
4. 刪除使用者

```shell
src/main/java
└── com
    └── transglobe
        └── framework
            └── examples
                └── crud
                    ├── CrudApplication.java
                    ├── complex  # 偏 DDD 架構寫法, 較多的物件轉換, 稍複雜
                    │   └── user
                    ├── rose  # 再保V3,法巴或保德信架構寫法, 只抽離 DTO
                    │   └── customer
                    └── traditional  # 傳統 CRUD 架構寫法, 從 DB 到 UI 都直接使用 Entity
                        └── client
```

> `user`, `customer`, 跟 `client` 的內容都一樣, 取不同名字只是方便撰寫範例
