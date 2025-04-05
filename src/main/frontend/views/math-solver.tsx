import React, {useCallback, useState} from "react"
import {Card, Notification, ProgressBar, Upload,} from "@vaadin/react-components"
import {ViewConfig} from "@vaadin/hilla-file-router/types.js"
import MathNode from "Frontend/components/MathMarkdown"

export const config: ViewConfig = {
    menu: {
        title: "이미지 분석후 수학문제 풀기",
    },
}

function ImageAnalysis() {
    const [loading, setLoading] = useState(false)
    const [imageUrl, setImageUrl] = useState("")
    const [analysisText, setAnalysisText] = useState("")
    const [youtubeUrls, setYoutubeUrls] = useState([])

    const handleFileUpload = useCallback(async ({file}: { file: any }) => {
        if (!file) return

        const formData = new FormData()
        formData.append("image", file)
        formData.append("message", "이 이미지에 무엇이 있나요?")

        setLoading(true)
        setImageUrl("")
        setAnalysisText("")
        setYoutubeUrls([])

        try {
            const response = await fetch("/image-text/analyze", {
                method: "POST",
                body: formData,
            })

            if (response.ok) {
                const {imageUrl, analysisText, youtubeUrls} = await response.json()
                setImageUrl(imageUrl)
                setAnalysisText(analysisText)  // 서버로부터 수신된 수학 문제와 풀이
                setYoutubeUrls(youtubeUrls || [])
            } else {
                Notification.show("이미지 분석에 실패했습니다. 다시 시도해주세요.")
            }
        } catch (error) {
            console.error("Error:", error)
            Notification.show("오류가 발생했습니다. 다시 시도해주세요.")
        } finally {
            setLoading(false)
        }
    }, [])

    return (
        <div className="container mt-5">
            <h2>이미지 업로드 및 분석</h2>
            <Upload
                accept=".jpg,.jpeg,.png"
                maxFiles={1}
                onUploadSuccess={({detail}) => handleFileUpload({file: detail.file})}
                style={{marginBottom: "1em"}}
                onFileReject={(event) => {
                    Notification.show(event.detail.error)
                }}
            />

            {loading && (
                <ProgressBar indeterminate className="mt-3">
                    {`이미지를 분석 중입니다... 잠시만 기다려주세요.`}
                </ProgressBar>
            )}

            {imageUrl && (
                <Card className="mt-5">
                    <div className="card-header">
                        <h5 className="mb-0">분석 결과</h5>
                    </div>
                    <div className="card-body">
                        <div className="row">
                            <div className="col-md-6 text-center">
                                <img
                                    src={imageUrl}
                                    className="img-fluid border rounded"
                                    alt="Uploaded"
                                />
                            </div>
                            <div className="col-md-6">
                                <h5>분석 내용</h5>
                                <MathNode>
                                    {analysisText}
                                </MathNode>
                            </div>
                        </div>

                        <hr/>

                        {youtubeUrls.length > 0 && (
                            <div>
                                <h5 className="mt-4">관련 YouTube 비디오</h5>
                                <div className="row mt-3">
                                    {youtubeUrls.map((videoId) => (
                                        <div className="col-md-6 mb-4" key={videoId}>
                                            <Card className="h-100">
                                                <iframe
                                                    src={`https://www.youtube.com/embed/${videoId}`}
                                                    allowFullScreen
                                                    className="card-img-top"
                                                    style={{width: "100%", height: "200px"}}
                                                ></iframe>
                                                <div className="card-body text-center">
                                                    <h6 className="card-title">관련 비디오</h6>
                                                </div>
                                            </Card>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                </Card>
            )}
        </div>
    )
}

export default ImageAnalysis
