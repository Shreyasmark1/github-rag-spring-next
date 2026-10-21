"use client"
import { BrandMark } from "@/components/layout/app-layout"
import { ModeToggle } from "@/components/theme-toggle"
import { AlertTitle, AlertDescription, Alert } from "@/components/ui/alert"
import { buttonVariants } from "@/components/ui/button"
import { CardHeader, CardTitle, CardDescription, CardContent, Card } from "@/components/ui/card"
import { Spinner } from "@/components/ui/spinner"
import { getGithubLoginUrl } from "@/lib/api"
import { AlertCircle, GithubIcon } from "@hugeicons/core-free-icons"
import { HugeiconsIcon } from "@hugeicons/react"
import { cn } from "cn"
import Link from "next/link"
import { useRouter, useSearchParams } from "next/navigation"
import { Suspense, useEffect } from "react"

function LoginLoading() {
    return (
        <div className="flex min-h-svh items-center justify-center">
            <Spinner className="size-8" />
        </div>
    )
}

const LoginContent = () => {

    const params = useSearchParams();
    const router = useRouter();
    const error = params.get("error");
    const next = params.get("next") || "/dashboard";
    const user = null;
    const isLoading = false;

    useEffect(() => {})

    if (isLoading || user) {
        return <LoginLoading />
    }

    return (
       <div className="relative flex min-h-svh flex-col overflow-hidden bg-background">
      <div className="pointer-events-none absolute inset-0 bg-[radial-gradient(ellipse_at_top,oklch(from_var(--primary)_l_c_h/0.1),transparent_55%)]" />

      <header className="relative z-10 flex h-14 items-center justify-between px-4">
        <Link href="/">
          <BrandMark />
        </Link>
        <ModeToggle />
      </header>

      <main className="relative z-10 flex flex-1 items-center justify-center px-4 py-10">
        <Card className="w-full max-w-sm border-border/70 bg-card/90 shadow-lg shadow-foreground/5 backdrop-blur-xl">
          <CardHeader className="space-y-4 text-center">
            <div className="mx-auto flex size-12 items-center justify-center rounded-2xl bg-foreground text-background">
              <HugeiconsIcon icon={GithubIcon} className="size-6" />
            </div>
            <div className="space-y-1">
              <CardTitle className="text-xl">Sign in</CardTitle>
              <CardDescription>
                Connect GitHub to chat with your repositories.
              </CardDescription>
            </div>
          </CardHeader>

          <CardContent className="space-y-4">
            {error && (
              <Alert variant="destructive">
                <HugeiconsIcon icon={AlertCircle} />
                <AlertTitle>Sign-in failed</AlertTitle>
                <AlertDescription>Please try again.</AlertDescription>
              </Alert>
            )}

            <a
              href={getGithubLoginUrl()}
              className={cn(
                buttonVariants({ size: "lg" }),
                "inline-flex w-full items-center justify-center gap-2 bg-foreground text-background hover:bg-foreground/90"
              )}
            >
              <HugeiconsIcon icon={GithubIcon} className="size-5" />
              Continue with GitHub
            </a>
          </CardContent>
        </Card>
      </main>
    </div>
    )
}

export default function LoginPage() {
    return (
        <Suspense fallback={<LoginLoading />}>
            <LoginContent />
        </Suspense>
    )
}