package de.infinity.util;

import de.infinity.parser.PathProperties;
import de.infinity.parser.UriOptions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonMappingFilter<T> {


    public void filter(List<T> objectList, UriOptions uriOptions) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        PathProperties pathProperties = uriOptions.getPathProperties();
        List<PathProperties.Props> props = new ArrayList<>(pathProperties.getPathProps());

        List<String> mainProps = new ArrayList<>();
        mainProps.addAll(props.get(0).getProperties());


        for(Object myObject : objectList) {
            Method[] allMethods = myObject.getClass().getMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                if(m.getGenericParameterTypes().length > 0) {
                    if(!mname.contains("set")) {
                        continue;
                    }

                    String shortMethodName = mname.substring(3,4).toLowerCase() + mname.substring(4);
                    if(!this.containsCaseInsensitive(shortMethodName, mainProps)) {
                        Method method = myObject.getClass().getMethod(mname, (Class)m.getGenericParameterTypes()[0]);
                        method.invoke(myObject, new Object[]{null});
                    }
                }
            }
        }
    }

    public void filter(Object object, String parameter) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Object> objectList = new ArrayList<>();
        objectList.add(object);
        this.filter(objectList, parameter);
    }


    private boolean containsCaseInsensitive(String strToCompare, List<String>list)
    {
        for(String str:list)
        {
            if(str.equalsIgnoreCase(strToCompare))
            {
                return(true);
            }
        }
        return(false);
    }
}
