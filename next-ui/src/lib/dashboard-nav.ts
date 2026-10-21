import type { IconSvgElement } from "@hugeicons/react";
import {
  FolderGit2Icon,
  LayoutGridIcon,
  Settings01Icon,
} from "@hugeicons/core-free-icons";

export type DashboardNavItem = {
  title: string;
  href: string;
  icon: IconSvgElement;
  exact?: boolean;
};

export type DashboardNavGroup = {
  label: string;
  items: DashboardNavItem[];
};

export const dashboardNavGroups: DashboardNavGroup[] = [
  {
    label: "Workspace",
    items: [
      {
        title: "Overview",
        href: "/dashboard/overview",
        icon: LayoutGridIcon,
      },
      {
        title: "Repositories",
        href: "/dashboard",
        icon: FolderGit2Icon,
        exact: true,
      },
    ],
  },
  {
    label: "Account",
    items: [
      {
        title: "Settings",
        href: "/dashboard/settings",
        icon: Settings01Icon,
      },
    ],
  },
];

export function isDashboardNavActive(
  pathname: string,
  href: string,
  exact = false
) {
  if (exact) {
    return pathname === href;
  }

  return pathname === href || pathname.startsWith(`${href}/`);
}