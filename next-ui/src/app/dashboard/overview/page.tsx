"use client";

import { RequireAuth } from "@/components/provider/require-auth";
import { AppShell } from "@/components/layout/app-layout";
import { OverviewDashboard } from "@/components/dashboard/overview-dashboard";

export default function OverviewPage() {
  return (
    <RequireAuth>
      <AppShell
        title="Overview"
        description="Workspace stats and recent repository activity"
      >
        <OverviewDashboard />
      </AppShell>
    </RequireAuth>
  );
}