export default function VideoGrid({ users }) {

    const getGirdCols = (count) => {
        if (count == 1) return "grid-cols-1";
        if (count <= 2) return "grid-cols-2";
        if (count <= 4) return "grid-cols-2";
        return "grid-cols-3";
    }

    const getGridRow = (count) => {
        if (count <= 2) return "grid-rows-1";
        if (count <= 4) return "grid-rows-2";
        return "grid-rows-2";
    }

    const getTileMaxSize = (count) => {
        if (count <= 2) return { width: "w-[99%]", height: "h-[99%]" };
        if (count <= 4) return { width: "w-[99%]", height: "h-[99%]" };
        return { width: "w-[99%]", height: "h-[99%]" };
    };

    const size = getTileMaxSize(users.length)

    return (
        <div className={`grid gap-2 p-4 w-full h-full ${getGirdCols(users.length)} ${getGridRow(users.length)} pb-24`}>
            {users.map((user) => (
                <div key={user.id} className={`relative bg-gray-800 rounded-lg overflow-hidden aspect-video shadow-lg ${size.width} ${size.height}`}>
                    <video 
                        className="w-full h-full object-cover"
                        autoPlay
                        playsInline
                        muted
                    />
                    <p className="absolute bottom-2 left-2 text-white text-sm">{user.nickname}</p>
                </div>
            ))}
        </div>
    )
}