import CopyLinkButton from "./CopyLinkButton";

export default function RoomHeader({ roomTitle, roomId }) {
    return (
        <div className="w-full sticky top-0 z-20 bg-gray-900 border-b border-gray-700 px-6 py-3 flex justify-between items-center shadow-md">
            <h1 className="text-lg font-semibold text-white truncate">
                {roomTitle}
            </h1>
            <CopyLinkButton roomId={roomId} />
        </div>
    )
}