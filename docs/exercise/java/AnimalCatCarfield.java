import java.util.ArrayList;
import java.util.List;

/**
 * extends只能接受本类及其子类
 * super只能接受本类及其父类
 */
public class AnimalCatCarfield {
    public static void main(String[] args) {
        List<Animal> animal = new ArrayList<Animal>();
        List<Cat> cat = new ArrayList<Cat>();
        List<Garfield> garfield = new ArrayList<Garfield>();

        animal.add(new Animal());
        cat.add(new Cat());
        garfield.add(new Garfield());

        // extends只能接受Cat类及其子类
        // List<? extends Cat> extendsCatFromAnimal = animal;
        // super只能接受Cat类及其父类
        List<? super Cat> superCatFromAnimal = animal;

        List<? extends Cat> extendsCatFromCat = cat;
        List<? super Cat> superCatFromCat = cat;

        List<? extends Cat> extendsCatFromGarfield = garfield;
        // List<? super Cat> superCatFromGarfield = garfield;

        // extends集合除null外，其他任何元素都不能add
        extendsCatFromCat.add(new Animal());
        extendsCatFromCat.add(new Cat());
        extendsCatFromCat.add(new Garfield());

        // super集合只能添加本类及其子类
        superCatFromCat.add(new Animal());
        superCatFromCat.add(new Cat());
        superCatFromCat.add(new Garfield());

        // extends集合 get操作 只能返回本类及其父类 类型，不能返回子类类型
        Object catExtends2 = extendsCatFromCat.get(0);
        Cat catExtends1 = extendsCatFromCat.get(0);
        Garfield garfield1 = extendsCatFromGarfield.get(0);


    }
}

class Animal{}
class Cat extends Animal{}
class Garfield extends Cat {}

