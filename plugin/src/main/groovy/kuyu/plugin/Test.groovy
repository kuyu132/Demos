package kuyu.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class Test implements Plugin<Project>{

    @Override
    void apply(Project project) {
    project.plugins.all {
        println("kuyuzhqi:"+it)
        if(it instanceof LibraryPlugin){
            android.libraryVariants.all{variant->
                variant.outputs.all{ output->
                    task
                }
            }
        }else if(it instanceof AppPlugin){

        }

      }
    }
}