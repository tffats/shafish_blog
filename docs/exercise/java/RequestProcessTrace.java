/**
 * InheritableThreadLocal父子线程共享线程变量问题
 */
public class RequestProcessTrace {
    private static final InheritableThreadLocal<FullLinkContext> FULL_LINK_THREADLOCAL = new InheritableThreadLocal<>();

    public static FullLinkContext getContext() {

        FullLinkContext fullLinkContext = FULL_LINK_THREADLOCAL.get();

        if(fullLinkContext == null) {
            FULL_LINK_THREADLOCAL.set(new FullLinkContext());
            fullLinkContext = FULL_LINK_THREADLOCAL.get();
        }
        return fullLinkContext;
    }

    public static class FullLinkContext {
        private String traceId;
        public String getTraceId() {
            if(StringUtils.isEmpty(traceId)) {
                FrameWork.startTrace(null, "gujin");
                traceId = FrameWork.getTraceId();
            }
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }
    }
}