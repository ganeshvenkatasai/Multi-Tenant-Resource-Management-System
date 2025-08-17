package com.example.multitenant.util;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        String t = CURRENT_TENANT.get();
        return (t == null || t.isBlank()) ? "public" : t;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
