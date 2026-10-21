"use client"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { PropsWithChildren, useState } from "react"

export const QueryProvider = ({ children }: PropsWithChildren) => {

    const [query, setQuery] = useState(() => new QueryClient())

    return (
        <QueryClientProvider client={query}>
            {children}
        </QueryClientProvider>
    )
}