package io.jenkins.plugins.devopsportal.reporters;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import io.jenkins.plugins.devopsportal.models.*;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Abstract class for BUILD activity reporters.
 *
 * @author Rémi BELLO {@literal <remi@evolya.fr>}
 */
public abstract class AbstractActivityReporter<T extends AbstractActivity> extends Builder
        implements SimpleBuildStep, GenericActivityHandler<T> {

    private String applicationName;
    private String applicationVersion;
    private String applicationComponent;

    public AbstractActivityReporter(String applicationName, String applicationVersion, String applicationComponent) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationComponent = applicationComponent;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @DataBoundSetter
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    @DataBoundSetter
    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getApplicationComponent() {
        return applicationComponent;
    }

    @DataBoundSetter
    public void setApplicationComponent(String applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public BuildStatus.DescriptorImpl getBuildStatusDescriptor() {
        return Jenkins.get().getDescriptorByType(BuildStatus.DescriptorImpl.class);
    }

    @Override
    public void perform(@NonNull Run<?, ?> run, @NonNull FilePath workspace, @NonNull EnvVars env,
                        @NonNull Launcher launcher, @NonNull TaskListener listener) {

        // Create or update BuildStatus
        getBuildStatusDescriptor().update(applicationName, applicationVersion, record -> {

            // Generic record data
            GenericRunModel.updateRecordFromRun(record, run, env);

            // Create or update AbstractActivity
            record.updateActivity(applicationComponent, getActivityCategory(), listener, env, this);

            listener.getLogger().printf(
                    "Report build activity '%s' for application '%s' version %s component '%s'%n",
                    getActivityCategory(),
                    record.getApplicationName(),
                    record.getApplicationVersion(),
                    applicationComponent
            );

        });
    }

    public abstract ActivityCategory getActivityCategory();

}
