import { useState } from "react";

export default function CopyLinkButton({ roomId }) {
    const [copied, setCopied] = useState(false);

    const handleCopy = async () => {
        try {
            const url = `${window.location.origin}/chatroom/${roomId}`;
            await navigator.clipboard.writeText(url);
            setCopied(true);
            setTimeout(() => setCopied(false), 2000);
        } catch (err) {
            console.error("링크 복사 실패: ", err);
        }
    }

    return (
        <button 
            onClick={handleCopy}
            className="text-sm bg-blue-600 text-white px-3 py-1.5 rounded hover:bg-blue-500 transition">
            {copied ? "복사됨!" : "초대 링크 복사"}
        </button>
    )
}