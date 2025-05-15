import { Outlet } from "react-router";

export default function AuthLayout() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-grey-100 p-6">
            <div className="w-full max-w-md bg-white shadow-md rounded-lg p-8">
                <Outlet />
            </div>
        </div>
    )
}