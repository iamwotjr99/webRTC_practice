import RoomCard from "./RoomCard";

export default function RoomCardList({ rooms }) {
    return (
        <section>
            <h2 className="text-lg font-semibold mb-4">참여할 수 있는 스터디룸</h2>
            <div className="grid gap-4">
                {rooms &&rooms.map((room) => (
                    <RoomCard key={room.roomId} room={room} />
                ))}
            </div>
        </section>
    )
}