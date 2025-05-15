import { Outlet } from "react-router";
import Sidebar from "../components/Sidebar";

export default function MainLayout() {
    return (
        <div className="flex">
            <Sidebar />
            <main className="ml-64 flex-1 bg-gray-50 p-6 min-h-screen">
                <Outlet />
            </main>
        </div>
    )
}