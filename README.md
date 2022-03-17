# WebPos

| 学号      | 姓名   |
| --------- | ------ |
| 191220162 | 张乐简 |

[TOC]

## 概述

​		利用thymeleaf和Spring MVC,完成WebPos的几个增删查改功能。

## 内容

### 关于Spring MVC 和thymeleaf

​		按教程，View层动态地从Model层中获取数据生成真正的html文件传给浏览器，并在用户执行一些行动时调用Controller层的对应处理函数来处理。在本项目中，View层通过Spring MVC和thymeleaf协同实现。

​		要在index.html中使用Model的数据，可以通过以下方式获取：

```html
                    <dl class="dlist-align">
                        <dt>Sub Total:</dt>
                        <dd class="text-right" th:text="${'$'+cart.total}"></dd>
                    </dl>
                    <dl class="dlist-align">
                        <dt>Total:</dt>
                        <dd class="text-right h4 b" th:text="${'$'+cart.total*1.12}"></dd>
                    </dl>
```

​		如上，thymeleaf将自动计算cart.total并填充到html中。它也支持如*1.12这样的简单运算。当用户进行某些请求时，也通过thymeleaf解析请求，并由Spring MVC框架调用Controller组件的对应处理函数。调用的函数和要传的参数由annotation决定，如下：

```html
                      <button type="button"
      					 class="m-btn btn btn-default">
 						 <a th:href="@{/decrease?pid={pid}(pid=${item.product.id})}">
                           <i class="fa fa-minus"></i>
                           </a>
                      </button>
```

```java
    @GetMapping("/decrease")
    public String decrease(@RequestParam(name = "pid") String pid, Model model) {
        posService.decrease(pid,1);
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }

```

### 增加商品至购物车

​		在添加商品的按钮处设置对应的超链接。

```html
				<a th:href="@{/add?pid={pid}(pid=${product.id})}" class="btn btn-primary btn-sm float-right"> <i class="fa fa-cart-plus"></i> Add </a>
```

​		这样一来便可调用有@GetMapping("/add")标签的Controller类的成员函数。

```java
    @GetMapping("/add")
    public String add(@RequestParam(name = "pid") String pid, Model model) {
        List<Item> items=posService.getCart().getItems();
        boolean exist=false;
        for (Item item:
             items) {
            if(item.getProduct().getId().equals(pid)){
                exist=true;
            }
        }
        if(exist){
            posService.increase(pid,1);
        }else {
            posService.add(pid, 1);
        }
        model.addAttribute("products", posService.products());
        model.addAttribute("cart", posService.getCart());
        return "index";
    }
```

​		首先检测购物车中是否已经有商品，再进行相应的添加或新增操作，最后更新Model。

### 增加/减少购物车内商品的量

​		为方便操作，为Cart和posService添加increase和decrease两个接口，用于直接增加或减少购物车中某个特定商品的数量。

```java
    //posService
    public boolean increase(String productId,int amount){
        Product product = posDB.getProduct(productId);
        if (product == null) return false;
        this.getCart().increase(productId,amount);
        return true;
    }

    public boolean decrease(String productId,int amount){
        Product product = posDB.getProduct(productId);
        if (product == null) return false;

        this.getCart().decrease(productId,amount);
        return true;
    }
    //...
    //Cart
    public boolean increase(String productId,int amount){
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                total+=item.getProduct().getPrice();
                item.setQuantity(item.getQuantity()+amount);
                return true;
            }
        }
        return false;
    }
    public boolean decrease(String productId,int amount){
        Item toDel=null;
        boolean del=false;
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                if(amount<item.getQuantity()){
                    item.setQuantity(item.getQuantity()-amount);
                    total-=item.getProduct().getPrice()*amount;
                    return true;
                }else{
                    del=true;
                    toDel=item;
                }
            }
        }
        if(del){
            total-=toDel.getQuantity()*toDel.getProduct().getPrice();
            items.remove(toDel);
            return true;
        }
        return false;
    }
```

​		注意减少商品时要进行商品是否被移除的判断。两者也要同时更新cart.total，以便计算总价。

### 移除购物车内商品/清空购物车

​		同样通过增加接口的方式来实现。

```java
	//posService
	public boolean del(String productId){
        Product product = posDB.getProduct(productId);
        if (product == null) return false;

        this.getCart().del(productId);
        return true;
    }
    public void clear(){
        this.getCart().clear();
    }
	//...
	//Cart
	public boolean del(String productId){
        Item toDel=null;
        for (Item item:
                items) {
            if(item.getProduct().getId().equals(productId)){
                toDel=item;
            }
        }
        if(toDel!=null){
            total-=toDel.getQuantity()*toDel.getProduct().getPrice();
            items.remove(toDel);
            return true;
        }
        return false;
    }
    public void clear(){
        total=0;
        items.clear();
    }

```

​		通过一层层封装，total的值得以正确更新。