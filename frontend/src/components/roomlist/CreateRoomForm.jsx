import { useState } from "react"
import Button from "../common/Button";
import Input from "../common/Input";

export default function CreateRoomForm() {
    const [roomTitle, setRoomTitle] = useState("");
    const [roomCapacity, setRoomCapacity] = useState("");

    const handleCreateRoom = (e) => {
        e.preventDefault();

        if (roomTitle.length <= 0) {
            return alert("스터디방 제목을 입력해주세요.")
        };

        const capacity = Number(roomCapacity);
        if(!capacity || capacity < 2 || capacity > 6) {
            return alert("인원수는 2명이상 6명 이하로 입력해주세요.");
        }

        alert(`"${roomTitle}"방을 ${capacity}명으로 생성합니다.`)
        setRoomTitle("");
        setRoomCapacity("");
    }

    return (
        <section className="bg-white p-6 rounded shadow max-w-2xl">
            <h2 className="text-lg font-semibold mb-2">새 스터디룸 만들기</h2>
            <form className="flex gap-2" onSubmit={handleCreateRoom}>
                <Input 
                    type="text"
                    value={roomTitle} 
                    onChange={(e) => setRoomTitle(e.target.value)}
                    placeholder="스터디룸 제목 입력 (예: 같이 리액트 공부해요)"
                    className="flex-1"
                    // error="문제 발생"
                />

                <select 
                    value={roomCapacity}
                    onChange={(e) => setRoomCapacity(e.target.value)}
                    className="border rounded px-3 py-2 w-40 text-base focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                    <option value="">최대 인원을 선택해주세요</option>
                    {[2, 3, 4, 5, 6].map((num) => (
                        <option key={num} value={num}>
                            {num}명
                        </option>
                    ))}
                </select>
                 
                <Button type="submit" variant="create" className="self-center">
                    방 만들기
                </Button>
            </form>
        </section>
    )
}