package tij.annotations;

import tij.annotations.util.UseCase;

import java.lang.reflect.Method;

public class UseCaseProcessor {
    public static void main(String[] args) {
        Method[] methods = PasswordUtils.class.getDeclaredMethods();
        for (Method m: methods){
            UseCase useCase = m.getAnnotation(UseCase.class);
            if(useCase != null){
                System.out.println("Found UseCase: "+useCase.id()+" "+useCase.description());
            }
        }
    }
}
