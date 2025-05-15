import { useState } from "react"
import Button from "./common/Button";
import Input from "./common/Input";

export default function CreateRoomForm() {
    const [roomTitle, setRoomTitle] = useState("");

    const handleCreateRoom = (e) => {
        e.preventDefault();
        if (roomTitle.length <= 0) return alert("스터디방 제목을 입력해주세요!");
        alert(`"${roomTitle}"방을 생성합니다.`)
        setRoomTitle("");
    }

    return (
        <section className="bg-white p-6 rounded shadow max-w-xl">
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
                <Button type="submit" variant="create" className="self-center">
                    방 만들기
                </Button>
            </form>
        </section>
    )
}