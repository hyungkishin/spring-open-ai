import {ViewConfig} from "@vaadin/hilla-file-router/types.js"
import {
    Button,
    Card,
    FormLayout,
    HorizontalLayout,
    Notification,
    ProgressBar,
    Select,
    TextField,
} from "@vaadin/react-components"
import {useEffect, useState} from "react"

export const config: ViewConfig = {
    menu: {
        title: "image generate",
    },
}

const MODEL_DALLE_2 = "dall-e-2"
const MODEL_DALLE_3 = "dall-e-3"

export default function ImageGenerate() {

    const modelItems = [
        {label: "DALL-E 2", value: MODEL_DALLE_2},
        {label: "DALL-E 3", value: MODEL_DALLE_3},
    ]

    const imageCountItems = [
        {label: "1", value: "1"},
        {label: "2", value: "2"},
        {label: "3", value: "3"},
    ]

    const [selectedModel, setSelectedModel] = useState("")
    const [imageCount, setImageCount] = useState("")
    const [message, setMessage] = useState("")
    const [loading, setLoading] = useState(false)
    const [imageUrls, setImageUrls] = useState<string[]>([])

    const responsiveSteps = [
        {minWidth: "0", columns: 1},
        {minWidth: "0", columns: 1},
    ]

    useEffect(() => {
        if (selectedModel === MODEL_DALLE_3) {
            setImageCount("1")
            Notification.show("DALL E 3 모델의 경우 최대 생성 가능한 갯수는 1 개 입니다.", {
                position: "middle",
            })
        }
    }, [selectedModel])

    const handleSubmit = async () => {
        if (!selectedModel || !imageCount || !message) {
            Notification.show("모든 필드를 입력해주세요.", {position: "middle"})
            return
        }

        try {
            setLoading(true)

            const response = await fetch("/v1/images/generate", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    model: selectedModel,
                    count: parseInt(imageCount),
                    message: message,
                }),
            })

            if (!response.ok) {
                Notification.show("이미지 생성 중 오류가 발생했습니다.", { position: "middle" })
                return
            }

            const result = await response.json()
            setImageUrls(result)
            Notification.show(result, {position: "middle"})
        } catch (error) {
            Notification.show("이미지 생성 중 오류가 발생했습니다.", {position: "middle"})
        } finally {
            setLoading(false)
        }
    }

    const downloadImage = (imageUrl: string) => {
        window.location.href = `/v1/images/download-file?url=${encodeURIComponent(imageUrl)}`
        Notification.show('이미를 다운로드 하였습니다 🥰', {position: "middle"})
    }

    return (
        <FormLayout responsiveSteps={responsiveSteps}>
            <HorizontalLayout style={{alignItems: "center"}}>
                <TextField
                    label="생성할 이미지에 대해서 설명해주세요"
                    placeholder="예) 고양이가 우주에서 춤추고 있는 모습"
                    style={{padding: 0, width: "33%"}}
                    onChange={(e) => setMessage(e.target.value)}
                    required
                />
            </HorizontalLayout>

            <HorizontalLayout theme="spacing" style={{alignItems: "center"}}>
                <Select
                    label="이미지 모델"
                    placeholder="모델을 선택하세요"
                    items={modelItems}
                    value={selectedModel}
                    onValueChanged={(e) => setSelectedModel(e.detail.value)}
                    required
                />

                <Select
                    label="생성할 이미지 갯수"
                    placeholder="갯수를 선택하세요"
                    items={imageCountItems}
                    value={imageCount}
                    onValueChanged={(e) => setImageCount(e.detail.value)}
                    required
                />
            </HorizontalLayout>
            <hr/>

            <Button onClick={handleSubmit}>이미지 생성</Button>

            {loading && (
                <div>
                    <label className="text-secondary" id="pblbl">
                        {"요청 이미지 생성중 ..."}
                    </label>
                    <ProgressBar indeterminate aria-labelledby="pblbl" aria-describedby="sublbl"/>
                </div>
            )}

            <div
                className="card-grid"
                style={{
                    gridTemplateColumns: `repeat(${imageUrls.length}, minmax(300px, 1fr))`,
                }}
            >
                {imageUrls.map((url, index) => (
                    <Card key={index} theme="cover-media" onClick={() => downloadImage(url)}>
                        <img slot="media" width="200" src={url} alt={`Generated image ${index + 1}`}/>
                        <div slot="title">{"이미지 생성 완료 !"}</div>
                        <div slot="subtitle">{`model: ${selectedModel}`}</div>
                        <div>{message}</div>
                    </Card>
                ))}
                <style>
                {`
                    .card-grid {
                        display: grid;
                        gap: 1em;
                    }
                `}
                </style>
            </div>
        </FormLayout>
    )
}
