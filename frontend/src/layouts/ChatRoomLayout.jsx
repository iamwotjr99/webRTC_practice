import { Outlet } from "react-router";

export default function ChatRoomLayout() {
    return (
        <div className="w-screen h-screen bg-black flex items-center justify-center relative flex-col">
            <Outlet />
        </div>
    )
}