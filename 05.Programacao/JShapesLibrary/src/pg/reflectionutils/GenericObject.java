package pg.reflectionutils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pg.graphicenviroment.interfaces.IActor;

import test.arkanoidPG.Ball;
import test.arkanoidPG.Brick;

public abstract class GenericObject  {
	
	private static boolean areParametersValid(Method methodOfUntyped, Method interfaceMethod) {
		Class<?>[] methodOfUntypedParams = methodOfUntyped.getParameterTypes();
		Class<?>[] methodOfInterfacesParams = interfaceMethod.getParameterTypes();
		Annotation[][] methodAnnotations = interfaceMethod.getParameterAnnotations();
		int i;
		for (i=0; i<methodOfUntypedParams.length && i<methodOfInterfacesParams.length; ++i) {
			if (isAnyAnnotationPresent(Any.class, methodAnnotations[i]))
				continue;
			if (!methodOfInterfacesParams[i].isAssignableFrom(methodOfUntypedParams[i]))
				return false;
		}
		return i == methodOfInterfacesParams.length;
	}

	private static boolean isAnyAnnotationPresent(Class<? extends Annotation> annotationClass, Annotation[] annotations) {
		for (Annotation a : annotations)
			if (a.annotationType().equals(annotationClass))
				return true;
		return false;
	}
	
	private static ArrayList<Class<?>> getRelevantParams(Method interfaceMethod) {
		Annotation[][] methodAnnotations = interfaceMethod.getParameterAnnotations();
		ArrayList<Class<?>> relevantParams = new ArrayList<Class<?>>();
		for (int i=0; i<methodAnnotations.length; ++i)
			if (methodAnnotations[i].length == 0 || !isAnyAnnotationPresent(IgnoreParam.class, methodAnnotations[i]))
				relevantParams.add(interfaceMethod.getParameterTypes()[i]);
		return relevantParams;
	}
	
	private static boolean isImplemented(Method interfaceMethod, Method[] allUntypedMethods) {
		//getRelevantParams(interfaceMethod);
		//Class<?>[] relevantParams = interfaceMethod.getParameterTypes();
		for (Method untypedObjectMethod : allUntypedMethods)
			if (untypedObjectMethod.getName().equals(interfaceMethod.getName()) && 
				compatibleReturnType(untypedObjectMethod, interfaceMethod) && 
				(interfaceMethod.getParameterTypes().length == 0 || 
				 areParametersValid(untypedObjectMethod, interfaceMethod)))
				return true;
		return false;
	}
	
	private static Method getMethodByName(Method[] allUntypedMethods, String name) {
		for (Method method : allUntypedMethods)
			if (method.getName().equals(name))
				return method;
		return null;
	}

	private static boolean compatibleReturnType(Method untypedObjectMethod, Method interfaceMethod) {
		if (interfaceMethod.getReturnType().isPrimitive())
			return untypedObjectMethod.getReturnType() == interfaceMethod.getReturnType();
		else {
			if (interfaceMethod.getReturnType().isAssignableFrom(untypedObjectMethod.getReturnType()))
				return true;
			else
				return isCompatibleWithInterface(untypedObjectMethod.getReturnType(), interfaceMethod.getReturnType());
		}
			
//		return true;
	}
	
	private static boolean isCompatibleWithInterface(Class<?> untypedClass, Class<?> interfaceClass) {
		boolean foundMustImplement = false;
		Method[] allUntypedMethods = untypedClass.getMethods();
		Method[] allInterfaceMethods = interfaceClass.getDeclaredMethods();
		for (Method method : allInterfaceMethods)
			if (method.isAnnotationPresent(MustImplement.class)) {
				if (!isImplemented(method, allUntypedMethods))
					return false;
				foundMustImplement = true;
			}
			else if (method.isAnnotationPresent(Optional.class)) {
				Method optionalMethod = getMethodByName(allUntypedMethods, method.getName());
				if (optionalMethod != null) {
					//ArrayList<Class<?>> relevantParams = getRelevantParams(method);
					//Class<?>[] relevantParams = method.getParameterTypes();
					return areParametersValid(optionalMethod, method);
				}
//				else {
//					return true;
//				}
			}
		return foundMustImplement;
	}
			
	public static boolean isCompatibleWithInterface(Object untyped, Class<?> interfaceClass) {
		if (untyped == null) return false;
		if (!interfaceClass.isAnnotationPresent(Proxyable.class)) return false;
		Class<?> untypedClass = untyped.getClass();
		return isCompatibleWithInterface(untypedClass, interfaceClass);
	}

	public static boolean isAssignableWith(Object untyped, Class<?> assignableClass) {
		return assignableClass.isAssignableFrom(untyped.getClass());
	}
	
	public static boolean hasMethod(Object obj, String methodName, Class<?> ... paramTypes) {
		Class<?> classOfObj = obj.getClass();
		boolean hasMethod = false;
		try {
			classOfObj.getDeclaredMethod(methodName, paramTypes);
			hasMethod = true;
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return hasMethod;
	}
	
	public static Method getDeclaredMethod(Class<?> classOfObj, String methodName, Class<?> ... paramTypes) {
		//Method[] methods = classOfObj.getDeclaredMethods();
		Method[] methods = classOfObj.getMethods();
		for (Method method : methods)
			if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), paramTypes))
				return method;
		return null;				
	}
		
	private HashMap<String, Method> methodsCache;
	protected Object object;
	protected Class<?> objectClass;

	private void init(Object obj) {
		this.object = obj;
		this.objectClass = obj.getClass();
		this.methodsCache = new HashMap<String, Method>();
	}
	
	GenericObject(Object obj) {
		init(obj);
	}

	protected GenericObject(Object obj, Class<?> interfaceClass) throws Exception {
		if (!isCompatibleWithInterface(obj, interfaceClass))
			throw new Exception("Incompatible interfaces: " + obj.getClass() + " with " + interfaceClass);
		init(obj);
	}

	public Class<?> getRealObjectClass() {
		return objectClass; 
	}

	public Object getRealObject() {
		// TODO Auto-generated method stub
		return object;
	}
	
	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof GenericObject)) return false;
		GenericObject genericObject = (GenericObject) object;
		return this.object.equals(genericObject.object);
	}
	
	protected Object call(String methodName, Object[] params, Class<?>[] paramTypes) {
		if (object == null)
			return null;
		Method method;
		Object ret = null;
		StringBuilder methodSignatureBuilder = new StringBuilder(methodName);
		for (Class<?> paramType : paramTypes)
			methodSignatureBuilder.append(paramType);
		String methodSignature = methodSignatureBuilder.toString();
		try {
			if ((method=methodsCache.get(methodSignature))!=null) {
				ret = method.invoke(object, params);
			} else if (!methodsCache.containsKey(methodSignature)) { //&& (method=methodsCache.get(methodSignature))==null)
				method = getDeclaredMethod(objectClass, methodName, paramTypes);
				methodsCache.put(methodSignature, method);
				if (method!=null)
					ret = method.invoke(object, params);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
//
//	@Override
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		System.out.println(object);
//		method.invoke(object, args);
//		return null;
//	}
//	
//	public static Object factory(Object realObject, Class<?> theInterface) {
//		Object proxy = Proxy.newProxyInstance(realObject.getClass().getClassLoader(), 
//							   new Class<?>[]{theInterface},
//							   new GenericObject(realObject));
//		return proxy;
//	}
//	
//	public static void main(String[] args) {
//		Ball b = new Ball(10, 20);
//		Object proxy = factory(b, IActor.class);
//		IActor iactor = (IActor) proxy;
//		iactor.onCollision(new Brick(20,30,40,50));
//	}
	
}