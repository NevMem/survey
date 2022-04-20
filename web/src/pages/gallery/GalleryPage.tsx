import { useParams } from "react-router-dom";
import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import useAsyncRequest, { isOk, RequestError } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import { Media, MediaGallery } from "../../data/exported";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import CardError from "../../app/card/CardError";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import TextButton from "../../components/button/TextButton";

const GalleryGrid = (props: { gallery: MediaGallery }) => {
    const { gallery } = props;

    const openMedia = (media: Media) => {
        window.open(media.url, '_blank')?.focus();
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={8}>
                {gallery.gallery.map((media, index) => {
                    return (
                        <SpaceBetweenRow key={index}>
                            <Text>{media.filename}</Text>
                            <TextButton onClick={() => openMedia(media)}>Просмотреть</TextButton>
                        </SpaceBetweenRow>
                    );
                })}
            </SpacedColumn>
        </OutlinedCard>
    );
};

const GalleryPageContent = () => {
    const { id } = useParams();

    const request = useAsyncRequest(controller => backendApi.gallery(controller, parseInt(id ?? "0")));

    if (isOk(request)) {
        return (
            <GalleryGrid gallery={request.result} />
        );
    } else if (request instanceof RequestError) {
        return (
            <CardError>
                {request.message}
            </CardError>
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader />
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

const GalleryPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Галерея</Text>
                <GalleryPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
}

export default GalleryPage;
