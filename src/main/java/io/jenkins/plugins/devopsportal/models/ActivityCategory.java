package io.jenkins.plugins.devopsportal.models;

public enum ActivityCategory {

    BUILD(BuildActivity.class),
    UNIT_TEST(UnitTestActivity.class),
    QUALITY_AUDIT(QualityAuditActivity.class),
    PERFORMANCE_TEST(PerformanceTestActivity.class),
    IMAGE_RELEASE(ImageReleaseActivity.class);

    private final Class<? extends AbstractActivity> clazz;

    ActivityCategory(Class<? extends AbstractActivity> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends AbstractActivity> getClazz() {
        return clazz;
    }

}
