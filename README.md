# Yating ASR Streaming SDK - Java version

## Available Key

- Please contact Yating ASR

## Task Flow

1. 跟 `Token Server` 連線，用 `API Key` 取得 `token`，`token` 為一次性使用。

1. 創建 `AsrResult`，用來儲存翻譯後的結果。

1. 創建 `MessageListener`，用來接聽並處理 `Streaming Server` 的回傳訊息。

1. 跟 `Streaming Server` 連線，建立連線後，將音訊資料送出。

1. 取得 `Streaming Server` 的回傳訊息，並將結果儲存至 `AsrResult`。

## 聲音格式限制

| Name                 |  Parameter Name  | Default Setting | Description                     |
| :------------------- | :--------------: | :-------------: | ------------------------------- |
| sample rate          |    sampleRate    |      16000      |                                 |
| sample size(in bits) | sampleSizeInBits |       16        | Also known as 2 Byte per frame. |
| channels             |     channels     |        1        |                                 |

1. 上述設定為 `ASR Streaming` 的默認設定，目前僅提供上述規格作為接受音訊資料的格式。

1. 使用不同的設定將會讓辨識結果受到影響，以致於無法正確辨識。

## Token Client

### Token API Input

```JSON
{
    "pipeline": "PIPELINE_NAME",
    "options": {
        "lang": "LANGUAGE_NAME"
    }
}
```

### Available Variable

| Pipeline Name      | Description  |
| ------------------ | ------------ |
| asr-stream-general | 即時語音辨識 |

| Language Name | Description                        |
| ------------- | ---------------------------------- |
| zhen          | 中文為主，並可處理英文夾雜的情況。 |
| zhtw          | 中文為主，並可處理台語夾雜的情況。 |
| en            | 全英文情境使用。                   |

### Token API Output

```JSON
{
    "auth_token":"AUTH_TOKEN_FOR_STREAMING",
    "success":true
}
```

## Streaming Client

### Streaming Server Url

使用在 `Token Client` 取得的 `auth_token`  來和 `Streaming Server` 建立連線。

```
https://{STREAMING_SERVER_URL}?token={AUTH_TOKEN_FOR_STREAMING}
```

### Messages

1. 成功和 `Streaming Server` 建立連線

```JSON
{
    "status":"ok",
    "ssid":"WEBSOCKET_SSID",
    "message_type":"session_started"
}
```

1. 取得辨識結果通道接通辨識標記

```JSON
{
    "pipe": {
        "asr_state": "first_chunk_received"
    }
}
```

1. 語音辨識，語音段落起始標記

```JSON
{
    "pipe": {
        "asr_state": "utterance_begin"
    }
}
```

1. 語音辨識，語音段落起始標記

```JSON
{
    "pipe": {
        "asr_state": "utterance_end"
    }
}
```

1. 語音辨識的臨時結果，方便給前端顯示使用。

```JSON
{
    "pipe": {
        "asr_sentence": "今天"
    }
}
{
    "pipe": {
        "asr_sentence": "今天天氣"
    }
}
{
    "pipe": {
        "asr_sentence": "今天天氣很"
    }
}
{
    "pipe": {
        "asr_sentence": "今天天氣很好"
    }
}
```

1. 語音辨識的最後結果。

```JSON
{
    "pipe": {
        "asr_final": true,
        "asr_begin_time": 0.032,
        "asr_end_time": 4.532,
        "asr_sentence": "今天天氣很好。",
        "asr_confidence": 1.0,
        "asr_word_time_stamp": [
            {
                "word": "今天",
                "begin_time": 0.4661875,
                "end_time": 0.54515625,
                "punctuator": ""
            },
            {
                "word": "天氣",
                "begin_time": 0.5846250000000001,
                "end_time": 0.70303125,
                "punctuator": ""
            },
            {
                "word": "很",
                "begin_time": 0.7425,
                "end_time": 1.09778125,
                "punctuator": ""
            },
            {
                "word": "好",
                "begin_time": 1.17671875,
                "end_time": 1.334625,
                "punctuator": "。"
            }
        ],
        "text_segmented": "今天 天氣 很 好"
    }
}
```

### Demo Example

1. 連結 `ASR Streaming Server` 將音檔進行語音辨識

   - 音檔需為 `wav` 格式

   - 音檔需符合 `聲音格式限制`

   成功連線後，將會讓音檔以模擬實際播放的形式將音訊資料發送給 `Streaming Server`，並取得辨識結果。

1. Capture audio from microphone

   使用裝置的麥克風作為接收裝置，並將音訊資料發送給 `Streaming Server`，並取得辨識結果。
