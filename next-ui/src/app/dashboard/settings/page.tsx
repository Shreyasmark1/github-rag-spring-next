"use client";

import { RequireAuth } from "@/components/provider/require-auth";
import { AppShell } from "@/components/layout/app-layout";
import { SettingsDashboard } from "@/components/dashboard/settings-dashboard";

export default function SettingsPage() {
  return (
    <RequireAuth>
      <AppShell
        title="Settings"
        description="Profile, appearance, and account preferences"
      >
        <SettingsDashboard />
      </AppShell>
    </RequireAuth>
  );
}