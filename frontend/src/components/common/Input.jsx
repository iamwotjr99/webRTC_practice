export default function Input({
    type = "text",
    name,
    value,
    onChange,
    placeholder,
    disabled = false,
    error = "",
    success = "",
    className = "",
}) {
    const baseStyle = "border rounded px-3 py-1.5 text-base focus:outline-none focus:ring-2 focus:ring-green-500 w-full"

    return (
        <div className={`relative w-full ${className}`}>
            <input 
                type={type}
                name={name}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                disabled={disabled}
                className={`${baseStyle}
                    ${error ? "border-red-500" : 'border-gray-300'}`}
            />
            {error && (
                <p className="mt-0.5 text-sm text-red-500">
                    {error}
                </p>
            )}
            {success && (
                <p className="mt-0.5 text-sm text-green-500">
                    {success}
                </p>
            )}
        </div>
    )
}