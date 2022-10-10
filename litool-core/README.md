# core

## lino

a option-like interface, have two implement `Some`,`None`. the `Some` represent the value is `present`

```java
List<Map<String,String>> map;
Object obj  = map;

Lino.of(obj)
        .cast(List.class)
        .filter(l->l.size()>1)
        .map(l->l.get(1))
        .cast(String.class,String.class)
        .map(m->m.get("a"))
        .ifPresent(e->System.out.println(e.length()))
        .ifAbsent(()->System.out.println("null"))
```

## lira

## lean

a tool to create bean from another bean, work like gson

```json
{"name":"1","bean": {"name": "2"},"beans": [{"name": "3"}]}
```

```java
class Bean<T> {
    private String name;
    private Bean bean;
    private List<T> beans;
}
```

```java
Map map = gson.fromJson(json, Map.class);
LiTypeToken<Bean<Bean>> parameterized = LiTypeToken.getParameterized(Bean.class, Bean.class);

Lean lean = new Lean();
Bean<Bean> bean = lean.fromBean(map, parameterized);
Assertions.assertEquals("3", bean.beans.get(0).name);


Bean<Bean> copy= lean.copyBean(map, parameterized);
lean.copyBean(bean, copy);
Assertions.assertEquals("3", copy.beans.get(0).name);
```

you can custom the detail of copy pojo

`LeanKey` to specific the copy another named field or map-value

```json
{"age": 1.0}
```

```java
class Bean {
    @LeanKey("age")
    private double fake;
}


```

## lilink

## liif 



