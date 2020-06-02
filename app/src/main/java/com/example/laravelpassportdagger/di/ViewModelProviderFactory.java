package com.example.laravelpassportdagger.di;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import java.util.Map;
import javax.inject.Inject;

//ok  jemo hapa ni lazima uweke javax.inject kwa provider...its the same thing
public class ViewModelProviderFactory implements ViewModelProvider.Factory {

    private static final String TAG = "ViewModelProviderFactory";

    private final Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> creators;

    @Inject
    public ViewModelProviderFactory(Map<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        javax.inject.Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) { // if the viewmodel has not been created

            // loop through the allowable keys (aka allowed classes with the @ViewModelKey)
            for (Map.Entry<Class<? extends ViewModel>, javax.inject.Provider<ViewModel>> entry : creators.entrySet()) {

                // if it's allowed, set the Provider<ViewModel>
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }

        // if this is not one of the allowed keys, throw exception
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        // return the Provider
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}